package com.talkpossible.project.domain.chatGPT.controller;

import com.talkpossible.project.domain.chatGPT.dto.request.UserChatRequest;
import com.talkpossible.project.domain.chatGPT.dto.response.ChatResponse;
import com.talkpossible.project.domain.chatGPT.service.ChatRememberService;
import com.talkpossible.project.domain.chatGPT.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;
    private final ChatRememberService chatRememberService;

    @GetMapping("/chatGPT")
    public void getquestion() {
        chatService.getDailyQuestions();
    }


    @GetMapping("/chatGPT/remember")
    public ResponseEntity<ChatResponse> getquestions(@RequestBody UserChatRequest userChatRequest) {
        return ResponseEntity.ok(chatRememberService.getDailyQuestions(userChatRequest));
    }
}
