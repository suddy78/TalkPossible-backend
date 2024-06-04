package com.talkpossible.project.domain.chatGPT.service;

import com.talkpossible.project.domain.chatGPT.cache.CacheService;
import com.talkpossible.project.domain.chatGPT.dto.request.*;
import com.talkpossible.project.domain.chatGPT.dto.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRememberService {
    
    private final CacheService cacheService;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private HttpEntity<ChatRequest2> getHttpEntity(ChatRequest2 chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openaiApiKey);

        HttpEntity<ChatRequest2> httpRequest = new HttpEntity<>(chatRequest, headers);
        return httpRequest;
    }

    public ChatResponse getDailyQuestions(UserChatRequest userChatRequest) {

        List<Message> history = new ArrayList<>();

        String query1 = "레스토랑에서 음식 주문을 하는 상황을 연습하려고 해.";
        query1 += "\n조건은 다음과 같아.";
        query1 += "\n1.너는 서버의 역할만 해줘. 처음 온 손님에게 응대를 해주면 돼.";
        query1 += "\n2.레스토랑은 음식을 파는 음식점이야 ";
        query1 += "\n3.답변 형식은 '서버: {진지한 질문}' 이 형식으로 적어줘";

        if(userChatRequest.cacheId()!=null) {
            history = cacheService.getValue(userChatRequest.cacheId());
        }

        history.add(new Message("system", query1));
        history.add(new Message("user", userChatRequest.message()));
        
        
        // Create a request
        ChatRequest2 request1 = new ChatRequest2(model, history);

        // Call the API
        RestTemplate restTemplate1 = new RestTemplate();
        ChatResponse response1 = restTemplate1.postForObject(apiUrl, getHttpEntity(request1), ChatResponse.class);

        if (response1 == null || response1.getChoices() == null || response1.getChoices().isEmpty()) {
            throw new RuntimeException();
        }
        
        history.add(new Message("system", response1.getChoices().get(0).getMessage().getContent()));
        String cacheId = cacheService.putValue(history);
        response1.setCacheId(cacheId);

//        String trueQuestion = response1.getChoices().get(0).getMessage().getContent().substring(8);
        return response1;


//        questionRepository.saveAll(newQuestionList);
    }
}