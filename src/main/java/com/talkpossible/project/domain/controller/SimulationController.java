package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.motion.response.UserMotionListResponse;
import com.talkpossible.project.domain.dto.simulation.response.BasicInfoResponse;
import com.talkpossible.project.domain.dto.simulations.response.UserSimulationResponse;
import com.talkpossible.project.domain.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/simulations")
    public ResponseEntity<UserSimulationResponse> createSimulation(
            @RequestHeader("patientId") Long patientId,
            @RequestHeader("situationId") Long situationId
    ) {
        // Simulation 생성 및 simulationId 반환
        UserSimulationResponse response = simulationService.createSimulation(patientId, situationId);

        // 응답 반환
        return ResponseEntity.ok(response);
    }
    
    // 피드백 조회 - 시뮬레이션 정보 & 영상
    @GetMapping("/simulations/{simulationId}/info")
    public ResponseEntity<BasicInfoResponse.Body> getBasicFeedback(@PathVariable long simulationId){

        BasicInfoResponse response = simulationService.getBasicFeedback(simulationId);

        return ResponseEntity.ok()
                .header("patientId", String.valueOf(response.getHeader().getPatientId()))
                .body(response.getBody());
    }

    // 피드백 조회 - 동작인식
    @GetMapping("/simulations/{simulationId}/motion")
    public ResponseEntity<UserMotionListResponse> getMotionFeedback(@PathVariable long simulationId) {

        return ResponseEntity.ok(simulationService.getMotionFeedback(simulationId));
    }

}
