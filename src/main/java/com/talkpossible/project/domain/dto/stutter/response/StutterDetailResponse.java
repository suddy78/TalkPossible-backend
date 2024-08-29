package com.talkpossible.project.domain.dto.stutter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StutterDetailResponse {

    private String imageUrl;

    private String audioUrl;

    private String word;

}
