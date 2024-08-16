package com.talkpossible.project.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;

}
