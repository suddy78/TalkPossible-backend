package com.talkpossible.project.domain.chatGPT.controller;

import com.talkpossible.project.domain.chatGPT.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/chatGPT")
    public void getquestion() {
        chatService.getDailyQuestions();
    }
}
