package com.talkpossible.project.domain.dto.stutter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class StutterDetailListResponse {

    @JsonProperty("image_url")
    private List<String> imageUrl;

    @JsonProperty("audio_url")
    private List<String> audioUrl;

    private List<String> words;

    // 리스트의 첫번째 요소값 추출
    public String getFirstImageUrl() {
        return (imageUrl != null && !imageUrl.isEmpty()) ? imageUrl.get(0) : null;
    }

    public String getFirstAudioUrl() {
        return (audioUrl != null && !audioUrl.isEmpty()) ? audioUrl.get(0) : null;
    }

    public String getFirstWords() {
        return (words != null && !words.isEmpty()) ? words.get(0) : null;
    }

    // StutterDetailResponse 객체로 변환
    public StutterDetailResponse toStutterDetailResponse() {
        return new StutterDetailResponse(
                getFirstImageUrl(),
                getFirstAudioUrl(),
                getFirstWords()
        );
    }

}
