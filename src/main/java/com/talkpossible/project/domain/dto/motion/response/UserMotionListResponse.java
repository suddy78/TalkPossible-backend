package com.talkpossible.project.domain.dto.motion.response;

import com.talkpossible.project.domain.domain.MotionDetail;
import lombok.Builder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record UserMotionListResponse(
        List<UserMotionDetailResponse> motionList,
        int motionCount
) {
    public static UserMotionListResponse of (final List<MotionDetail> motionDetails) {
        return UserMotionListResponse.builder()
                .motionList(motionDetails.stream().map(UserMotionDetailResponse::of).toList())
                .motionCount(motionDetails.size())
                .build();
    }

}
