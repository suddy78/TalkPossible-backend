package com.talkpossible.project.domain.chatGPT.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.talkpossible.project.domain.chatGPT.cache.CacheService;
import com.talkpossible.project.domain.chatGPT.dto.request.ChatStreanRequest;
import com.talkpossible.project.domain.chatGPT.dto.request.Message;
import com.talkpossible.project.domain.chatGPT.dto.request.UserChatRequest;
import com.talkpossible.project.domain.chatGPT.dto.response.ChatStreamResponse;
import com.talkpossible.project.domain.chatGPT.dto.response.ChatStreamUserResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatStreamService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final CacheService cacheService;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .build();
    }

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    public Flux<ChatStreamUserResponse> askToGpt(UserChatRequest userChatRequest) throws JsonProcessingException {

        List<Message> history = new ArrayList<>();

        String query = "레스토랑에서 음식 주문을 하는 상황을 연습하려고 해.";
        query += "\n조건은 다음과 같아.";
        query += "\n모든 답변은 한국어로 하고, 존댓말을 사용해.";
        query += "\n1.너는 서버의 역할만 해줘. 처음 온 손님에게 응대를 해주면 돼.";
        query += "\n2.레스토랑은 음식을 파는 음식점이야 ";
//        query += "\n3.답변 형식은 'AI: {진지한 질문}' 이 형식으로 적어줘";
        query += "\n사용자가 메뉴판을 요청하면 메뉴를 임의로 생성해서 이름과 가격 정보를 제공하는데, '[{번호}] {메뉴} ({가격})' 형식으로 적어줘.";
        query += "\n음식에 대해 물어볼 경우 너가 만든 메뉴판을 바탕으로 상세 설명을 해줘.";
        query += "\n모든 문장 끝에는 마침표(온점, 물음표, 느낌표)를 붙여줘.";

        if(userChatRequest.cacheId() != null) {
            history = cacheService.getValue(userChatRequest.cacheId());
        }

        history.add(new Message("system", query));
        history.add(new Message("user", userChatRequest.message()));

        // Request 생성
        ChatStreanRequest request = new ChatStreanRequest(model, history);

        // GPT API 호출
        Flux<String> eventStream = webClient.post()
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        String cacheId = cacheService.createCacheId();

        return processEventStream(cacheId, history, eventStream);
    }

    private Flux<ChatStreamUserResponse> processEventStream(String cacheId, List<Message> history, Flux<String> eventStream) {

        StringBuilder answer = new StringBuilder();
        Flux<ChatStreamUserResponse> response = eventStream
                .filter(chunk -> !chunk.contains("DONE"))
                .flatMap(chunk -> {
                    try {
                        ChatStreamResponse gptResponse = objectMapper.readValue(chunk, ChatStreamResponse.class);
                        String content = gptResponse.getChoices().get(0).getDelta().getContent();
                        return Flux.just(content);
                    } catch (JsonProcessingException e) {
//                        log.error("processEventStream( ) - JsonProcessingException");
                        return Flux.error(e);
                    }
                })
                .filter(content -> !content.trim().isEmpty())
                .bufferUntil(content -> content.endsWith(".") || content.endsWith("?") || content.endsWith("!") || content.endsWith("\n"))
                .map(contents -> {
                    String completeSentence = String.join("", contents);
                    answer.append(completeSentence);
                    return new ChatStreamUserResponse(cacheId, completeSentence);
                })
                .doOnComplete(() -> {
                    history.add(new Message("assistant", answer.toString()));
                    cacheService.putValue(cacheId, history);
                })
                .concatWith(Flux.just(new ChatStreamUserResponse(cacheId, "END_OF_STREAM")));

        return response;
    }
}
