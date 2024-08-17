package com.talkpossible.project.domain.chatGPT.domain;

import com.talkpossible.project.domain.chatGPT.domain.common.BaseTimeEntity;
import com.talkpossible.project.domain.login.dto.SignupRequest;
import com.talkpossible.project.global.config.type.Role;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Builder
    private Doctor(String name, String email, String password,
                   String phoneNum, Role role, String refreshToken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public static Doctor create(SignupRequest request, String encryptedPassword, Role role) {
        return Doctor.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encryptedPassword)
                .phoneNum(request.getPhoneNum())
                .role(role)
                .refreshToken(null)
                .build();
    }

}
