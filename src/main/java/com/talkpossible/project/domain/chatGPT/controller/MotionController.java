package com.talkpossible.project.domain.chatGPT.controller;

import com.talkpossible.project.domain.chatGPT.dto.motion.request.UserMotionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MotionController {

    @PostMapping("/motion")
    private ResponseEntity<Void> saveUserMotion(
            @RequestBody UserMotionRequest userMotionRequest
    ) {

    }
}
