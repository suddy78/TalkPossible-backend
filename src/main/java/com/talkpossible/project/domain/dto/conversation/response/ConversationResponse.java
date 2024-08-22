package com.talkpossible.project.domain.dto.conversation.response;

import com.talkpossible.project.domain.domain.Conversation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ConversationResponse {

    private List<Message> conversationList;

    public static ConversationResponse from(List<Message> messageList){
        return ConversationResponse.builder()
                .conversationList(messageList)
                .build();
    }

    @Builder
    @Getter
    public static class Message {
        String speaker;
        String content;

        public static Message from(Conversation conversation){
            return Message.builder()
                    .speaker(conversation.getPatient() == null ? "chatgpt" : "patient")
                    .content(conversation.getContent())
                    .build();
        }
    }

}
