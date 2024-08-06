package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.MotionDetail;
import com.talkpossible.project.domain.domain.Patient;
import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.dto.motion.request.UserMotionRequest;
import com.talkpossible.project.domain.repository.MotionDetailRepository;
import com.talkpossible.project.domain.repository.PatientRepository;
import com.talkpossible.project.domain.repository.SimulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MotionService {

    private final MotionDetailRepository motionDetailRepository;
    private final SimulationRepository simulationRepository;
    private final PatientRepository patientRepository;

    public void saveUserMotion(UserMotionRequest userMotionRequest) {
        Simulation simulation = simulationRepository.findById(userMotionRequest.simulationId())
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 시뮬레이션 정보를 찾을 수 없습니다."));

        List<MotionDetail> motionDetails = userMotionRequest.motionList().stream().map(
                motion -> MotionDetail.of(simulation, userMotionRequest, motion)).toList();

        Patient patient = patientRepository.findById(userMotionRequest.patientId())
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 환자 정보를 찾을 수 없습니다."));

        simulation.updateRunDateAndTotalTime(userMotionRequest.runDate(), userMotionRequest.totalTime());

        motionDetailRepository.saveAll(motionDetails);
    }
}
