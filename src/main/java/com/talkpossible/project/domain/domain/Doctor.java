package com.talkpossible.project.domain.domain;

import com.talkpossible.project.domain.dto.doctor.request.SignupRequest;
import com.talkpossible.project.global.enumType.Role;
import jakarta.persistence.*;
import lombok.*;
import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Doctor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "doctor_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNum;

    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Builder
    private Doctor(String name, String email, String password,
                   String profileImgUrl, String phoneNum,
                   Role role, String refreshToken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImgUrl = profileImgUrl;
        this.phoneNum = phoneNum;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public static Doctor create(SignupRequest request, String encryptedPassword, Role role) {
        return Doctor.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encryptedPassword)
                //.profileImgUrl(profileImgUrl)
                .phoneNum(request.getPhoneNum())
                .role(role)
                .refreshToken(null)
                .build();
    }

}
