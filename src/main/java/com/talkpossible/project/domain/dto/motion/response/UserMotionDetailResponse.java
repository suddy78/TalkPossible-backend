package com.talkpossible.project.domain.dto.motion.response;

import com.talkpossible.project.domain.domain.MotionDetail;
import lombok.Builder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record UserMotionDetailResponse(
        String motionName,
        String timestamp
) {
    public static UserMotionDetailResponse of(final MotionDetail motionDetail) {
        return UserMotionDetailResponse.builder()
                .motionName(motionDetail.getMotionName())
                .timestamp(motionDetail.getTimestamp() != null ? motionDetail.getTimestamp().toString() : null)
                .build();
    }
}
