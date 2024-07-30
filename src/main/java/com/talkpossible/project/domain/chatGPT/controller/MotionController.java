package com.talkpossible.project.domain.chatGPT.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MotionController {

    private ResponseEntity<Void> saveUserMotion() {

    }
}
