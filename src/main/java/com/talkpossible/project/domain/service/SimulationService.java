package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.dto.simulation.response.BasicInfoResponse;
import com.talkpossible.project.domain.repository.SimulationRepository;
import com.talkpossible.project.global.exception.CustomException;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.talkpossible.project.global.exception.CustomErrorCode.ACCESS_DENIED;
import static com.talkpossible.project.global.exception.CustomErrorCode.SIMULATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final SimulationRepository simulationRepository;


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
