package com.talkpossible.project.domain.dto.motion.request;

import java.sql.Time;
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
            Time timestamp
    ) {
    }
}
