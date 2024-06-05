package com.talkpossible.project.domain.chatGPT.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatStreamResponse {
    private String content;
}
