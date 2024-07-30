package com.talkpossible.project.domain.chatGPT.dto.chat.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMemorizeMessageDto {
    private String cacheId;
    private List<ChatMessage> chatList;

}