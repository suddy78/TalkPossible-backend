package com.talkpossible.project.domain.dto.motion.request;

import java.util.List;

public record UserMotionRequest(
        Long simulationId,
        Long patientId,
        String runDate,
        List<Motion> motionList,
        String videoUrl,
        String totalTime
) {
    public record Motion(
            String motionName,
            String timestamp
    ) {
    }
}
