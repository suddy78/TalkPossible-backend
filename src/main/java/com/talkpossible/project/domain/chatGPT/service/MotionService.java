package com.talkpossible.project.domain.chatGPT.service;

import com.talkpossible.project.domain.chatGPT.dto.motion.request.UserMotionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MotionService {

    public void saveUserMotion(UserMotionRequest userMotionRequest) {
        userMotionRequest.motionList().stream().map(a -> )
    }
}
