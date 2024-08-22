package com.talkpossible.project.domain.dto.situation.response;

import com.talkpossible.project.domain.domain.Situation;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record SituationDetailResponse(
        long situationId,
        String imgUrl,
        String title,
        String intro,
        String description
) {
    public static SituationDetailResponse of (final Situation situation) {
        return SituationDetailResponse.builder()
                .situationId(situation.getId())
                .imgUrl(situation.getThumbnailUrl())
                .title(situation.getTitle())
                .intro(situation.getIntro())
                .description(situation.getDescription())
                .build();
    }
}
