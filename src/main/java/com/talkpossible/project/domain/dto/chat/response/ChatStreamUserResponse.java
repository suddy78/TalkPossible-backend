package com.talkpossible.project.domain.dto.chat.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자에게 전송할 GPT API 응답 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatStreamUserResponse {
    private String cacheId;
    private String content;
}
