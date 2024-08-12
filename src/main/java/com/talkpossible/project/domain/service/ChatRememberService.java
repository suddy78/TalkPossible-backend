package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.cache.CacheService;
import com.talkpossible.project.domain.dto.chat.request.ChatRequest;
import com.talkpossible.project.domain.dto.chat.request.Message;
import com.talkpossible.project.domain.dto.chat.request.UserChatRequest;
import com.talkpossible.project.domain.dto.chat.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.util.*;

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

    private Map<String, List<String>> restaurantMenuMap;
    private String selectedRestaurant;
    private List<String> selectedMenu;

    private final Random random = new Random();

    @PostConstruct
    public void init() {
        restaurantMenuMap = new HashMap<>();

        restaurantMenuMap.put("한식", Arrays.asList("김치찌개", "불고기", "비빔밥", "잡채", "삼겹살"));
        restaurantMenuMap.put("중식", Arrays.asList("짜장면", "탕수육", "짬뽕", "마파두부", "꿔바로우"));
        restaurantMenuMap.put("일식", Arrays.asList("초밥", "라멘", "우동", "가츠동", "데리야끼 치킨"));
        restaurantMenuMap.put("이탈리안식", Arrays.asList("피자", "파스타", "리조또", "카프레제", "티라미수"));
        restaurantMenuMap.put("멕시칸식", Arrays.asList("타코", "부리또", "퀘사디아", "엔칠라다", "과카몰리"));

    }

    private void selectRandomRestaurantAndMenu() {
        List<String> restaurants = new ArrayList<>(restaurantMenuMap.keySet());
        System.out.println(random.nextInt(restaurants.size()));
        selectedRestaurant = restaurants.get(random.nextInt(restaurants.size()));
        selectedMenu = restaurantMenuMap.get(selectedRestaurant);
    }

    private HttpEntity<ChatRequest> getHttpEntity(ChatRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openaiApiKey);

        return new HttpEntity<>(chatRequest, headers);
    }

    public ChatResponse getGPTAnswer(UserChatRequest userChatRequest) {

        selectRandomRestaurantAndMenu();

        List<Message> history = new ArrayList<>();

        String query1 = "레스토랑에서 음식 주문을 하는 상황을 연습하려고 해.";
        query1 += "\n조건은 다음과 같아.";
        query1 += "\n1.너는 서버의 역할만 해줘. 처음 온 손님에게 응대를 해주면 돼.";
        query1 += "\n2.레스토랑은 음식을 파는 음식점이야 ";
        query1 += "\n3.답변 형식은 '서버: {진지한 질문}' 이 형식으로 적어줘";
        query1 += "\n1. 너는 서버의 역할만 해줘. 처음 온 손님에게 응대를 해주면 돼.";
        query1 += "\n2. 레스토랑은 " + selectedRestaurant + "이야.";
        query1 += "\n3. 메뉴는 " + String.join(", ", selectedMenu) + "를 포함해 여러 가지가 있어.";
        query1 += "\n4. 답변 형식은 '서버: {진지한 질문}' 이 형식으로 적어줘";

        if(userChatRequest.cacheId()!=null) {
            history = cacheService.getValue(userChatRequest.cacheId());
        } else {
            history.add(new Message("system", query1));
        }

        history.add(new Message("user", userChatRequest.message()));

        // Create a request
        ChatRequest request = new ChatRequest(model, history);

        // Call the API
        RestTemplate restTemplate1 = new RestTemplate();
        ChatResponse response = restTemplate1.postForObject(apiUrl, getHttpEntity(request), ChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException();
        }

        history.add(new Message("system", response.getChoices().get(0).getMessage().getContent()));
        String cacheId = cacheService.putValue(history);
        response.setCacheId(cacheId);

        return response;
    }
}
