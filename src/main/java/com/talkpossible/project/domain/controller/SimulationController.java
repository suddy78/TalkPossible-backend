package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.simulations.request.UpdateSimulationRequest;
import com.talkpossible.project.domain.dto.simulations.response.UserSimulationResponse;
import com.talkpossible.project.domain.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
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

    @PostMapping("/simulations/{simulationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 응답 본문 없으므로, 204 No Content 상태 코드 반환되면 잘 된 것!
    public void updateSimulation(
            @RequestHeader("patientId") Long patientId,
            @PathVariable("simulationId") Long simulationId,
            @RequestBody UpdateSimulationRequest request
    ) {
        simulationService.updateSimulation(patientId, simulationId, request);
    }
}
