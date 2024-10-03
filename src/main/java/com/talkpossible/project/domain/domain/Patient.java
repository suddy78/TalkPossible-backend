package com.talkpossible.project.domain.domain;


import com.talkpossible.project.domain.domain.common.BaseTimeEntity;
import com.talkpossible.project.domain.dto.doctor.request.SignupRequest;
import com.talkpossible.project.domain.dto.patient.request.PostPatient;
import com.talkpossible.project.global.enumType.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private String name;

    private LocalDate birthday;

    private String phoneNum;

    @Column(nullable = false)
    private boolean gender;

    @Builder
    private Patient(Doctor doctor, String name, LocalDate birthday, String phoneNum, boolean gender) {
        this.doctor = doctor;
        this.name = name;
        this.birthday = birthday;
        this.phoneNum = phoneNum;
        this.gender = gender;
    }

    public static Patient create(Doctor doctor, PostPatient postPatient) {
        return Patient.builder()
                .doctor(doctor)
                .name(postPatient.name())
                .birthday(LocalDate.parse(postPatient.birthday()))
                .phoneNum(postPatient.phoneNum())
                .gender(postPatient.gender())
                .build();
    }
}
