package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.motion.response.UserMotionListResponse;
import com.talkpossible.project.domain.dto.simulation.request.UpdateSimulationRequest;
import com.talkpossible.project.domain.dto.simulation.response.BasicInfoResponse;
import com.talkpossible.project.domain.dto.simulation.response.PatientSimulationDetailResponse;
import com.talkpossible.project.domain.dto.simulation.response.PatientSimulationListResponse;
import com.talkpossible.project.domain.dto.simulation.response.UserSimulationResponse;
import com.talkpossible.project.domain.dto.speechrate.request.SpeechRateRequest;
import com.talkpossible.project.domain.service.SimulationService;

import com.talkpossible.project.domain.service.StutterDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SimulationController {

    private final SimulationService simulationService;
    private final StutterDetailService stutterDetailService;

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

        // 말더듬 분석
        stutterDetailService.saveStutterDetail(simulationId, request.getVName());

        // 200 OK 상태 코드를 반환
        return ResponseEntity.ok().build();
    }

    
    // 피드백 조회 - 시뮬레이션 정보 & 영상
    @GetMapping("/simulations/{simulationId}/info")
    public ResponseEntity<BasicInfoResponse.Body> getBasicFeedback(@PathVariable long simulationId) {

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

    // 환자별 시뮬레이션 정보 조회
    @GetMapping("/simulations/{patientId}")
    public ResponseEntity<PatientSimulationListResponse> getPatientSimulationInfo(@PathVariable long patientId) {

        return ResponseEntity.ok(simulationService.getPatientSimulationInfo(patientId));
    }

    // 시뮬레이션 종료 후, 발화 속도 측정
    @PostMapping("/simulations/{simulationId}/speech-rate")
    public ResponseEntity<Void> addSpeechRate(@PathVariable long simulationId, @RequestBody SpeechRateRequest speechRateRequest) {

        simulationService.saveSpeechRate(simulationId, speechRateRequest);

        return ResponseEntity.ok().build();
    }

}
