package com.talkpossible.project.domain.dto.doctor.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {

    private String profileImgUrl;
    private String name;

    public static ProfileResponse from(String profileImgUrl, String name){
        return ProfileResponse.builder()
                .profileImgUrl(profileImgUrl)
                .name(name)
                .build();
    }

}
