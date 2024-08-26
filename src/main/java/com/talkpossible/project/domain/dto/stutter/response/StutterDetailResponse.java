package com.talkpossible.project.domain.dto.stutter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StutterDetailResponse {

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("audio_url")
    private String audioUrl;

    private String words;
}
