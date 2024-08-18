package com.talkpossible.project.domain.dto.chat.request;

import lombok.Getter;

/**
 * ChatController 에서 스트리밍 응답 테스트용 사용하는 UserChatRequest DTO
 */
@Getter
public class UserChatStreamRequest {
    String message;
}
