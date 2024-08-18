package com.talkpossible.project.domain.dto.chat.request;

public record ChatMessage(
        String role,
        String message
) {
    public static ChatMessage of(String role, String message) {
        return new ChatMessage(role, message);
    }
}
