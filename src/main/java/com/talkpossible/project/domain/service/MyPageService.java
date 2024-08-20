package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Patient;
import com.talkpossible.project.domain.dto.patient.reponse.PatientListResponse;
import com.talkpossible.project.domain.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final PatientRepository patientRepository;

    public PatientListResponse getPatientList(final long doctorId) {
        return PatientListResponse.of(patientRepository.findAllBydoctorId(doctorId));
    }
}
