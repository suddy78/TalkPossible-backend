package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.MotionDetail;
import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.dto.motion.request.UserMotionRequest;
import com.talkpossible.project.domain.repository.MotionDetailRepository;
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

    public void saveUserMotion(UserMotionRequest userMotionRequest) {
        List<MotionDetail> motionDetails = userMotionRequest.motionList().stream().map(
                motion -> MotionDetail.of(null, userMotionRequest, motion)).toList();

        motionDetailRepository.saveAll(motionDetails);
    }
}
