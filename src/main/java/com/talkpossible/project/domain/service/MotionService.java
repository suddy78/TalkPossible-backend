package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.MotionDetail;
import com.talkpossible.project.domain.domain.Patient;
import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.dto.motion.request.UserMotionRequest;
import com.talkpossible.project.domain.repository.MotionDetailRepository;
import com.talkpossible.project.domain.repository.PatientRepository;
import com.talkpossible.project.domain.repository.SimulationRepository;
import com.talkpossible.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.talkpossible.project.global.exception.CustomErrorCode.SIMULATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MotionService {

    private final MotionDetailRepository motionDetailRepository;
    private final SimulationRepository simulationRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public void saveUserMotion(UserMotionRequest userMotionRequest) {

        //Simulation simulation = simulationRepository.findById(userMotionRequest.simulationId())
        //        .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 시뮬레이션 정보를 찾을 수 없습니다."));
        Simulation simulation = simulationRepository.findByIdWithLock(userMotionRequest.simulationId())
                .orElseThrow(() -> new CustomException(SIMULATION_NOT_FOUND));

        List<MotionDetail> motionDetails = userMotionRequest.motionList().stream().map(
                motion -> MotionDetail.of(simulation, userMotionRequest, motion)).toList();

        Patient patient = patientRepository.findById(userMotionRequest.patientId())
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 환자 정보를 찾을 수 없습니다."));

        simulation.updateRunDateAndTotalTime(
                userMotionRequest.runDate(),
                userMotionRequest.totalTime(),
                userMotionRequest.videoUrl()
        );

        motionDetailRepository.saveAll(motionDetails);
    }
}
