package com.talkpossible.project.domain.dto.stutter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class StutterDetailListResponse {

    @JsonProperty("image_url")
    private List<String> imageUrlList;

    @JsonProperty("audio_url")
    private List<String> audioUrlList;

    @JsonProperty("words")
    private List<String> sentenceList;

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
                                .build()
                );
            }
        }

        return stutterList;
    }

}
