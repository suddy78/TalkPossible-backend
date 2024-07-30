package com.talkpossible.project.domain.chatGPT.dto.motion.request;

import java.util.List;

public record UserMotionRequest(
        Long situationId,
        Long patientId,
        String situationDate,
        List<Motion> motionList
) {
    public record Motion(
            String motionName,
            String timestamp
    ) {
    }
}
