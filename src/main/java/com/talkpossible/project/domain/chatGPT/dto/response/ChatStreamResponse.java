package com.talkpossible.project.domain.chatGPT.dto.response;

import lombok.Data;

import java.util.List;

/**
 * GPT API 응답을 매핑하기 위한 DTO
 */
@Data
public class ChatStreamResponse {

    private List<Choice> choices;

    @Data
    public static class Choice {
        private Delta delta;
    }

    @Data
    public static class Delta {
        private String content = "";
    }
}
