package com.talkpossible.project.domain.chatGPT.dto.chat.response;

import com.talkpossible.project.domain.chatGPT.dto.chat.request.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private String cacheId;

    private List<Choice> choices;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {

        private int index;
        private Message message;

    }

}