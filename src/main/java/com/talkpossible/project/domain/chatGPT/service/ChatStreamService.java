package com.talkpossible.project.domain.chatGPT.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.talkpossible.project.domain.chatGPT.dto.request.ChatStreanRequest;
import com.talkpossible.project.domain.chatGPT.dto.response.ChatStreamResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ChatStreamService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

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
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE );

    public Flux<ChatStreamResponse> askToGpt(String question) throws JsonProcessingException {

        ChatStreanRequest request = new ChatStreanRequest(model, question);

        String requestValue = objectMapper.writeValueAsString(request);

        Flux<String> eventStream = webClient.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        return processEventStream(eventStream);
    }

    private Flux<ChatStreamResponse> processEventStream(Flux<String> eventStream) {

        return eventStream
                .filter(chunk -> !chunk.contains("DONE"))
                .flatMap(chunk -> {
                    try {
                        JsonNode node = objectMapper.readTree(chunk);
                        String content = node.at("/choices/0/delta/content").asText();
                        return Flux.just(content);
                    } catch (JsonProcessingException e) {
//                        log.error("processEventStream( ) - JsonProcessingException");
                        return Flux.error(e);
                    }
                })
                .bufferUntil(content -> content.endsWith(".") || content.endsWith("?") || content.endsWith("!"))
                .map(contents -> {
                    String completeSentence = String.join("", contents);
                    return new ChatStreamResponse(completeSentence);
                });
    }
}
