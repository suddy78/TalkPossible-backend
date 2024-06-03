package com.talkpossible.project.domain.chatGPT.dto.request;

public record UserChatRequest(
        String cacheId,

        String message

) {
}
