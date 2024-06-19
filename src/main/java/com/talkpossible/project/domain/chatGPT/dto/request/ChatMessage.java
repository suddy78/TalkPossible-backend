package com.talkpossible.project.domain.chatGPT.dto.request;

public record ChatMessage(
        String role,
        String message
) {
    public static ChatMessage of(String role, String message) {
        return new ChatMessage(role, message);
    }
}
