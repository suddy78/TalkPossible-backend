package com.talkpossible.project.domain.chatGPT.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.talkpossible.project.domain.chatGPT.dto.request.UserChatRequest;
import com.talkpossible.project.domain.chatGPT.dto.request.UserChatStreamRequest;
import com.talkpossible.project.domain.chatGPT.dto.response.ChatResponse;
import com.talkpossible.project.domain.chatGPT.dto.response.ChatStreamResponse;
import com.talkpossible.project.domain.chatGPT.service.ChatRememberService;
import com.talkpossible.project.domain.chatGPT.service.ChatService;
import com.talkpossible.project.domain.chatGPT.service.ChatStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;
    private final ChatRememberService chatRememberService;
    private final ChatStreamService chatStreamService;

    @GetMapping("/chatGPT")
    public void getquestion() {
        chatService.getDailyQuestions();
    }


    @GetMapping("/chatGPT/remember")
    public ResponseEntity<ChatResponse> getquestions(@RequestBody UserChatRequest userChatRequest) {
        return ResponseEntity.ok(chatRememberService.getDailyQuestions(userChatRequest));
    }
    @GetMapping(value = "/chatGPT/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatStreamResponse> getAiResponseStreaming(@RequestBody UserChatStreamRequest userChatRequest) throws JsonProcessingException {
        return chatStreamService.askToGpt(userChatRequest.getMessage());
    }

}
