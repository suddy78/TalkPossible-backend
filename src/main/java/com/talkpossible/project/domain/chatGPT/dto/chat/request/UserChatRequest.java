package com.talkpossible.project.domain.chatGPT.dto.chat.request;

public record UserChatRequest(
        String cacheId,

        String message

) {
}
