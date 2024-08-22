package com.talkpossible.project.domain.dto.patient.reponse;

import com.talkpossible.project.domain.domain.Patient;

import java.util.List;

public record PatientListResponse(
        List<PatientDetailResponse> patients
) {
    public static PatientListResponse of(final List<Patient> patientList) {
        return new PatientListResponse(patientList.stream().map(PatientDetailResponse::of).toList());
    }
}
