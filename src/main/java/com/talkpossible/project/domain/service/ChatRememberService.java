package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.cache.CacheService;
import com.talkpossible.project.domain.domain.Conversation;
import com.talkpossible.project.domain.domain.Patient;
import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.dto.chat.request.ChatRequest;
import com.talkpossible.project.domain.dto.chat.request.Message;
import com.talkpossible.project.domain.dto.chat.request.UserChatRequest;
import com.talkpossible.project.domain.dto.chat.response.ChatResponse;
import com.talkpossible.project.domain.repository.ConversationRepository;
import com.talkpossible.project.domain.repository.PatientRepository;
import com.talkpossible.project.domain.repository.SimulationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRememberService {

    private final CacheService cacheService;
    private final PatientRepository patientRepository;
    private final SimulationRepository simulationRepository;
    private final ConversationRepository conversationRepository;

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
        selectedRestaurant = restaurants.get(random.nextInt(restaurants.size()));
        selectedMenu = restaurantMenuMap.get(selectedRestaurant);
    }

    private HttpEntity<ChatRequest> getHttpEntity(final ChatRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openaiApiKey);

        return new HttpEntity<>(chatRequest, headers);
    }

    public ChatResponse getGPTAnswerAboutRestaurant(final UserChatRequest userChatRequest, final long simulationId) {

        selectRandomRestaurantAndMenu();

        String query1 = "레스토랑에서 음식을 주문하는 상황을 연습하려고 해.";
        query1 += "\n다음 조건을 잘 지켜줘.";
        query1 += "\n1. 너는 레스토랑의 점원 역할을 맡아. 고객에게 첫 인사를 하고 응대해줘.";
        query1 += "\n2. 레스토랑의 이름은 " + selectedRestaurant + "이야.";
        query1 += "\n3. 제공하는 메뉴는 " + String.join(", ", selectedMenu) + " 등 여러 가지가 있어.";
        query1 += "\n4. 고객이 주문할 때 메뉴 추천을 부탁할 수 있어. 그럴 경우에는 친절하게 추천해줘.";
        query1 += "\n5. 주문을 받을 때 항상 존댓말로 응대하고, 상냥한 태도를 유지해줘.";
        query1 += "\n6. 주문을 완료한 후에는 고객에게 음료나 후식 등의 추가적인 질문도 해줘.";
        query1 += "\n7. 답변에는 이모지나 행동 묘사를 포함하지 않고, 순수한 텍스트 내용만 제공해줘.";

        return callGPT(userChatRequest, query1, simulationId);
    }

    public ChatResponse getGPTAnswerAboutLibrary(final UserChatRequest userChatRequest, final long simulationId) {

        String query1 = "서점에서 책을 찾거나 책을 구매하려는 상황을 연습하려고 해.";
        query1 += "\n다음 조건을 잘 지켜줘.";
        query1 += "\n1. 너는 서점에서 일하는 직원 역할을 맡아. 고객이 요청할 때마다 상냥하게 응대해줘.";
        query1 += "\n2. 고객이 찾는 책이 있으면 해당 책을 찾아주고, 만약 구매하려는 고객이면 책 찾기 대신 결제를 도와줘.";
        query1 += "\n3. 대화는 고객과 최소 4번 이상 주고받도록 해줘. 구체적인 질문이나 추가 요청을 할 수 있도록 유도해줘.";
        query1 += "\n4. 항상 존댓말을 사용하고, 친절하게 대화를 이끌어줘.";
        query1 += "\n5. 책을 찾거나 구매를 도운 후, 다른 책 추천이나 서점 서비스에 대한 안내도 친절하게 제공해줘.";
        query1 += "\n6. 답변에는 이모지나 행동 묘사를 포함하지 않고, 순수한 텍스트 내용만 제공해줘.";

        return callGPT(userChatRequest, query1, simulationId);
    }

    public ChatResponse getGPTAnswerAboutHairSalon(final UserChatRequest userChatRequest, final long simulationId) {

        String query1 = "미용실에서 방문한 고객에게  상황에서의 대화를 연습하려고 해.";
        query1 += "\n너는 미용실에서 커트, 펌, 염색을 해주는 미용사야. ";
        query1 += "\n조건은 다음과 같아.";
        query1 += "\n0.첫 인사말을 건낼 때는 " +
                "먼저 적절한 미용실 이름을 매번 새롭게 생성하고 그 이름을 포함한 문장으로 간단한 인사와 미용실 소개와 너의 소개를 한 다음, 미용실에 온 목적을 묻는 형식으로 응답을 구성해 줘. " +
                "예를 들어 '안녕하세요! 준오헤어입니다. 오늘 어떻게 도와드릴까요?'와 같이 대화를 시작해 줘.";
        query1 += "\n1.일관되게 존댓말을 사용하고, 손님에게 친절한 말투로 대해줘.";
        query1 += "\n2.모든 응답은 공백을 포함했을 때 반드시 70자 이내로 되도록 작성해 줘. 이를 초과할 경우 응답을 핵심만 간결하게 다시 구성해 줘.";
        query1 += "\n3.손님이 단답으로 대답하더라도 대화를 마무리 하지말고 손님에게 더 구체적인 질문을 던져줘.";
        query1 += "\n4.한 응답에는 추가적인 질문이 필요하더라도, 반드시 1개의 질문만 포함하도록 응답을 구성해 줘.";
        query1 += "\n5.해당 미용실에는 클리닉, 파마, 컷트, 염색 총 4가지의 서비스를 제공해.";
        query1 += "\n6.손님이 컷트를 하고 싶어한다면, 선호하는 머리 기장을 질문해줘. 손님이 파마를 하고 싶어한다면, 원하는 머리색이 무엇인지 질문해줘" +
                "손님이 클리닉을 하고 싶어한다면, 두피클리닉을 원하는지 모발 클리닉을 원하는지 질문해줘, 손님이 파마를 하고 싶어한다면, 원하는 펌 스타일을 질문해줘.";
        query1 += "\n7.대화는 최소 4번이상 주고받을 수 있도록 해줘.";
        query1 += "\n8.답변에는 이모지나 행동 묘사를 포함하지 않고, 순수한 텍스트 내용만 제공해줘.";

        return callGPT(userChatRequest, query1, simulationId);
    }

    public ChatResponse getGPTAnswerAboutHospital(final UserChatRequest userChatRequest, final long simulationId) {

        String query1 = "병원에서 환자가 의사에게 자신의 상황을 설명하고 질문하는 상황에서의 대화를 연습하려고 해.";
        query1 += "\n너는 병원에서 환자를 진료 후, 약물을 처방하거나 특정 검사를 지시하는 의사야. ";
        query1 += "\n조건은 다음과 같아.";
        query1 += "\n0.첫 인사말을 건낼 때는 " +
                "먼저 적절한 병원 이름을 매번 새롭게 생성하고 그 이름을 포함한 문장으로 간단한 인사와 병원 소개를 한 다음, 병원에 온 목적을 묻는 형식으로 응답을 구성해 줘. " +
                "예를 들어 '안녕하세요! 아임오케이 병원입니다. 어디가 불편하셔서 오셨나요?'와 같이 대화를 시작해 줘.";
        query1 += "\n1.일관되게 존댓말을 사용하고, 환자에게 친절한 말투로 대해줘.";
        query1 += "\n2.모든 응답은 공백을 포함했을 때 반드시 70자 이내로 되도록 작성해 줘. 이를 초과할 경우 응답을 핵심만 간결하게 다시 구성해 줘.";
        query1 += "\n3.환자가 단답으로 대답하더라도 자신의 상황에 대해서 더 구체적으로 설명할 수 있도록 유도하는 질문을 던져줘.";
        query1 += "\n4.한 응답에는 추가적인 질문이 필요하더라도, 반드시 1개의 질문만 포함하도록 응답을 구성해 줘.";
        query1 += "\n5.질병에 대해 의학적 용어만을 사용한 상세한 설명은 피하되, " +
                "의사로서 직접 환자의 상황을 진단하고 필요한 경우 간단한 설명 및 조언을 제공해 줘.";
        query1 += "\n6.의사로서 환자가 자신의 상태나 증상, 그로 인해 겪는 어려움, 약물 복용 이력 등에 대해 " +
                "더 자세히 설명하고 질문할 수 있도록 도와주는 질문을 던져줘.";
        query1 += "\n7.대화는 최소 4번이상 주고받을 수 있도록 해줘.";
        query1 += "\n8.환자가 최소 2번 이상 자신의 상태를 설명하였고 환자에 대한 정보를 충분히 얻었다면, " +
                "약물 처방 또는 검사 지시 후 다음 진료 또는 검사 예약으로 대화가 자연스럽게 마무리될 수 있도록 해줘.";
        query1 += "\n9.답변에는 이모지나 행동 묘사를 포함하지 않고, 순수한 텍스트 내용만 제공해줘.";

        return callGPT(userChatRequest, query1, simulationId);

    }

    private ChatResponse callGPT(UserChatRequest userChatRequest, String query, Long simulationId) {
        List<Message> history = new ArrayList<>();

        if(userChatRequest.cacheId()!=null) {
            history = cacheService.getValue(userChatRequest.cacheId());
        } else {
            history.add(new Message("system", query));
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

        Message GptMessage = response.getChoices().get(0).getMessage();

        // 새로운 Conversation 객체 생성
        Simulation simulation = getSimulation(simulationId);

        if(userChatRequest.message() != null && !userChatRequest.message().isEmpty()) {
            Conversation conversation = Conversation.builder()
                    .simulation(simulation)
                    .patient(simulation.getPatient())
                    .content(userChatRequest.message())
                    .sendTime(LocalDateTime.now()) // 현재 시간을 보내는 시간으로 설정
                    .build();

            conversationRepository.save(conversation);
        }

        // gpt 대화내용 데이터베이스에 저장
        conversationRepository.save(Conversation.create(
                simulation, null,
                GptMessage.getContent(), LocalDateTime.now()
        ));

        history.add(new Message("system", GptMessage.getContent()));
        String cacheId = cacheService.putValue(history);
        response.setCacheId(cacheId);

        return response;
    }

    private Simulation getSimulation(final long simulationId) {
        return simulationRepository.findById(simulationId)
                .orElseThrow(() -> new NoSuchElementException("시뮬레이션 정보를 찾을 수 없습니다."));
    }

}
