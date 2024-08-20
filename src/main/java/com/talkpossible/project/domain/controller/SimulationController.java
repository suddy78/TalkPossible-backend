package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.simulations.request.UpdateSimulationRequest;
import com.talkpossible.project.domain.dto.simulations.response.UserSimulationResponse;
import com.talkpossible.project.domain.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    // 시뮬레이션 생성
    @PostMapping("/simulations")
    public ResponseEntity<UserSimulationResponse> createSimulation(
            @RequestHeader("patientId") Long patientId,
            @RequestHeader("situationId") Long situationId
    ) {
        // Simulation 생성 및 simulationId 반환
        UserSimulationResponse response = simulationService.createSimulation(patientId, situationId);

        // 응답 반환 (200 OK)
        return ResponseEntity.ok(response);
    }

    // Conversation 추가
    @PostMapping("/simulations/{simulationId}")
    public ResponseEntity<Void> addConversation(
            @RequestHeader("patientId") Long patientId,
            @PathVariable("simulationId") Long simulationId,
            @RequestBody UpdateSimulationRequest request
    ) {
        simulationService.addConversation(patientId, simulationId, request);

        // 200 OK 상태 코드를 반환
        return ResponseEntity.ok().build();
    }
}
