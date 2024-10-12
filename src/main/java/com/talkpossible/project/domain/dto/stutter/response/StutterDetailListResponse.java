package com.talkpossible.project.domain.dto.stutter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.*;

@Getter
public class StutterDetailListResponse {

    // Stutter type 매핑
    private static final Map<String, String> STUTTER_TYPE_MAP = Map.of(
            "Sound Repetition", "음절 반복",
            "Prolongation", "연장"
    );

    @JsonProperty("image_url")
    private List<String> imageUrlList;

    @JsonProperty("audio_url")
    private List<String> audioUrlList;

    @JsonProperty("words")
    private List<String> sentenceList;

    @JsonProperty("stutter_type")
    private List<String> stutterTypeList;

    public List<StutterDetailResponse> filterUniqueSentences(){
        Set<String> uniqueAudioUrls = new HashSet<>();
        List<StutterDetailResponse> stutterList = new ArrayList<>();

        for(int i = 0; i < audioUrlList.size(); i++){
            if(uniqueAudioUrls.add(audioUrlList.get(i))) {
                stutterList.add(
                        StutterDetailResponse.builder()
                                .imageUrl(imageUrlList.get(i))
                                .audioUrl(audioUrlList.get(i))
                                .word(sentenceList.get(i))
                                .type(STUTTER_TYPE_MAP.getOrDefault(stutterTypeList.get(i), "결과 없음"))
                                .build()
                );
            }
        }

        return stutterList;
    }

}
