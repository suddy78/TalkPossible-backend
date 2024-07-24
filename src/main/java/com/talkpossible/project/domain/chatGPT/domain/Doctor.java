package com.talkpossible.project.domain.chatGPT.domain;

import com.talkpossible.project.domain.chatGPT.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

    private String phone_num;

}
