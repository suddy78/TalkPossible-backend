package com.talkpossible.project.domain.chatGPT.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.talkpossible.project.domain.chatGPT.dto.chat.request.UserChatRequest;
import com.talkpossible.project.domain.chatGPT.dto.chat.response.ChatResponse;
import com.talkpossible.project.domain.chatGPT.dto.chat.response.ChatStreamUserResponse;
import com.talkpossible.project.domain.chatGPT.service.ChatRememberService;
import com.talkpossible.project.domain.chatGPT.service.ChatStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatRememberService chatRememberService;
    private final ChatStreamService chatStreamService;

    @PostMapping("/chatGPT/remember")
    public ResponseEntity<ChatResponse> getquestions(@RequestBody UserChatRequest userChatRequest) {
        return ResponseEntity.ok(chatRememberService.getGPTAnswer(userChatRequest));
    }

    @PostMapping(value = "/chatGPT/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatStreamUserResponse> getAiResponseStreaming(@RequestBody UserChatRequest userChatRequest) throws JsonProcessingException {
        return chatStreamService.askToGpt(userChatRequest);
    }

}