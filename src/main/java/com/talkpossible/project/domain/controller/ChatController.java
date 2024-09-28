package com.talkpossible.project.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.talkpossible.project.domain.dto.chat.request.UserChatRequest;
import com.talkpossible.project.domain.dto.chat.response.ChatResponse;
import com.talkpossible.project.domain.dto.chat.response.ChatStreamUserResponse;
import com.talkpossible.project.domain.service.ChatRememberService;
import com.talkpossible.project.domain.service.ChatStreamService;
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

    @PostMapping("/chatGPT/restaurant")
    public ResponseEntity<ChatResponse> getResponseAboutRestaurant(
            @RequestBody UserChatRequest userChatRequest,
            @RequestHeader long simulationId
    ) {
        return ResponseEntity.ok(chatRememberService.getGPTAnswerAboutRestaurant(userChatRequest, simulationId));
    }

    @PostMapping("/chatGPT/library")
    public ResponseEntity<ChatResponse> getResponseAboutLibrary(
            @RequestBody UserChatRequest userChatRequest,
            @RequestHeader long simulationId
    ) {
        return ResponseEntity.ok(chatRememberService.getGPTAnswerAboutLibrary(userChatRequest, simulationId));
    }

    @PostMapping(value = "/chatGPT/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatStreamUserResponse> getAiResponseStreaming(@RequestBody UserChatRequest userChatRequest) throws JsonProcessingException {
        return chatStreamService.askToGpt(userChatRequest);
    }

}