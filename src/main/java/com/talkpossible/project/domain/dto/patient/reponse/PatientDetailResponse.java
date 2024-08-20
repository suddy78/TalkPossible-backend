package com.talkpossible.project.domain.dto.patient.reponse;

import com.talkpossible.project.domain.domain.Patient;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record PatientDetailResponse(
        long patientId,
        String name,
        String birth
) {
    public static PatientDetailResponse of(final Patient patient) {
        return PatientDetailResponse.builder()
                .patientId(patient.getId())
                .name(patient.getName())
                .birth(String.valueOf(patient.getBirthday()))
                .build();
    }
}
