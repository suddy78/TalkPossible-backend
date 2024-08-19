package com.talkpossible.project.domain.dto.situation.response;

import com.talkpossible.project.domain.domain.Situation;

import java.util.List;

public record SituationListResponse(
        List<SituationDetailResponse> situations
) {
    public static SituationListResponse of(final List<Situation> situationList) {
        return new SituationListResponse(
                situationList.stream().map(SituationDetailResponse::of).toList()
        );
    }
}
