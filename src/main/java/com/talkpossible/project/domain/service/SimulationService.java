package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Patient;
import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.domain.Situation;
import com.talkpossible.project.domain.dto.simulations.response.UserSimulationResponse;
import com.talkpossible.project.domain.repository.PatientRepository;
import com.talkpossible.project.domain.repository.SimulationRepository;
import com.talkpossible.project.domain.repository.SituationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final PatientRepository patientRepository;
    private final SituationRepository situationRepository;

    @Transactional
    public UserSimulationResponse createSimulation(Long patientId, Long situationId) {
        // Patient와 Situation 객체를 데이터베이스에서 조회
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patientId: " + patientId));
        Situation situation = situationRepository.findById(situationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid situationId: " + situationId));

        // Simulation 객체 생성
        Simulation simulation = Simulation.builder()
                .patient(patient)
                .situation(situation)
                .build();

        // Simulation 객체를 데이터베이스에 저장
        Simulation savedSimulation = simulationRepository.save(simulation);

        // 생성된 Simulation의 ID를 담아 응답
        return new UserSimulationResponse(savedSimulation.getId());
    }
}
