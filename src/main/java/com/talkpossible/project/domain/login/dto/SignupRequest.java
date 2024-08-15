package com.talkpossible.project.domain.login.dto;

import lombok.Getter;

@Getter
public class SignupRequest {

    private String name;
    private String phoneNum;
    private String email;
    private String password;

}