package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.conversation.response.ConversationResponse;
import com.talkpossible.project.domain.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConversationController {

    private final ConversationService conversationService;

    // 피드백 조회 - 대화 내용
    @GetMapping("/simulations/{simulationId}/conversation")
    public ResponseEntity<ConversationResponse> getConversationFeedback(@PathVariable long simulationId){
        return ResponseEntity.ok(conversationService.getConversationFeedback(simulationId));
    }

}
