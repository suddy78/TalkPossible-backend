package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.simulation.response.BasicInfoResponse;
import com.talkpossible.project.domain.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SimulationController {

    private final SimulationService simulationService;

    // 피드백 조회 - 시뮬레이션 정보 & 영상
    @GetMapping("/simulations/{simulationId}/info")
    public ResponseEntity<BasicInfoResponse.Body> getBasicFeedback(@PathVariable long simulationId){

        BasicInfoResponse response = simulationService.getBasicFeedback(simulationId);

        return ResponseEntity.ok()
                .header("patientId", String.valueOf(response.getHeader().getPatientId()))
                .body(response.getBody());
    }

}
