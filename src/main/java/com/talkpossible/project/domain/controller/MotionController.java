package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.motion.request.UserMotionRequest;
import com.talkpossible.project.domain.service.MotionService;
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

    private final MotionService motionService;

    @PostMapping("/motion")
    public ResponseEntity<Void> saveUserMotion(
            @RequestBody UserMotionRequest userMotionRequest
    ) {
        motionService.saveUserMotion(userMotionRequest);
        return ResponseEntity.ok().build();
    }
}
