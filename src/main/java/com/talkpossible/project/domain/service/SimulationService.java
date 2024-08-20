package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.domain.Patient;
import com.talkpossible.project.domain.domain.Situation;
import com.talkpossible.project.domain.dto.simulation.response.BasicInfoResponse;
import com.talkpossible.project.domain.dto.simulations.response.UserSimulationResponse;
import com.talkpossible.project.domain.repository.PatientRepository;
import com.talkpossible.project.domain.repository.SimulationRepository;
import com.talkpossible.project.domain.repository.SituationRepository;
import com.talkpossible.project.global.exception.CustomException;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.talkpossible.project.global.exception.CustomErrorCode.ACCESS_DENIED;
import static com.talkpossible.project.global.exception.CustomErrorCode.SIMULATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final JwtTokenProvider jwtTokenProvider;
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

    // 피드백 조회 - 시뮬레이션 정보 & 영상
    public BasicInfoResponse getBasicFeedback(long simulationId) {

        Long doctorId = jwtTokenProvider.getDoctorId();
        Simulation simulation = getSimulation(simulationId);

        if(doctorId != simulation.getPatient().getDoctor().getId()){
            throw new CustomException(ACCESS_DENIED);
        }

        return BasicInfoResponse.from(simulation, simulation.getPatient());
    }

    private Simulation getSimulation(final long simulationId) {
        return simulationRepository.findById(simulationId)
                .orElseThrow(() -> new CustomException(SIMULATION_NOT_FOUND));
    }

}
