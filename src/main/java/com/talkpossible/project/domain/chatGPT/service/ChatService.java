package com.talkpossible.project.domain.chatGPT.service;

import com.talkpossible.project.domain.chatGPT.dto.request.Question;
import com.talkpossible.project.domain.chatGPT.dto.request.ChatRequest;
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
public class ChatService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private HttpEntity<ChatRequest> getHttpEntity(ChatRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openaiApiKey);

        HttpEntity<ChatRequest> httpRequest = new HttpEntity<>(chatRequest, headers);
        return httpRequest;
    }

    public void getDailyQuestions() {

        List<Question> newQuestionList = new ArrayList<>();

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        Long randomNum = Long.valueOf(random.nextInt(308) + 1);

        String query1 = "레스토랑에서 음식 주문을 하는 상황을 연습하려고 해.";
        query1 += "\n조건은 다음과 같아.";
        query1 += "\n1.너는 서버의 역할만 해줘. 처음 온 손님에게 응대를 해주면 돼.";
        query1 += "\n2.레스토랑은 음식을 파는 음식점이야 ";
        query1 += "\n3.답변 형식은 '서버: {진지한 질문}' 이 형식으로 적어줘";

        // Create a request
        ChatRequest request1 = new ChatRequest(model, query1);

        // Call the API
        RestTemplate restTemplate1 = new RestTemplate();
        ChatResponse response1 = restTemplate1.postForObject(apiUrl, getHttpEntity(request1), ChatResponse.class);

        if (response1 == null || response1.getChoices() == null || response1.getChoices().isEmpty()) {
            throw new RuntimeException();
        }

        String trueQuestion = response1.getChoices().get(0).getMessage().getContent().substring(8);
//        Question newQuestion1 = new Question(trueQuestion, getCurrentDate(), Boolean.TRUE, null);
//        newQuestionList.add(newQuestion1);

        System.out.println(trueQuestion);

//        questionRepository.saveAll(newQuestionList);
    }
}