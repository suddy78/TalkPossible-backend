package com.talkpossible.project.domain.dto.stutter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StutterDetailResponse {

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("audio_url")
    private String audioUrl;

    private String words;

}
