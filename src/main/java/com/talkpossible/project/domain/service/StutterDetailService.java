package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.domain.StutterDetail;
import com.talkpossible.project.domain.dto.simulation.request.UpdateSimulationRequest;
import com.talkpossible.project.domain.dto.stutter.response.StutterDetailListResponse;
import com.talkpossible.project.domain.dto.stutter.response.StutterDetailResponse;
import com.talkpossible.project.domain.repository.SimulationRepository;
import com.talkpossible.project.domain.repository.StutterDetailRepository;
import com.talkpossible.project.global.exception.CustomErrorCode;
import com.talkpossible.project.global.exception.CustomException;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.talkpossible.project.global.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StutterDetailService {

    private final SimulationRepository simulationRepository;
    private final StutterDetailRepository stutterDetailRepository;
    private final JwtTokenProvider jwtTokenProvider;  // JwtTokenProvider 추가
    private final WebClient webClient;

    @Transactional(readOnly = true)
    public List<StutterDetailResponse> getStutterDetails(Long simulationId) {
        // 권한 확인 로직 추가
        Long doctorId = jwtTokenProvider.getDoctorId();
        Simulation simulation = getSimulation(simulationId);

        if (doctorId != simulation.getPatient().getDoctor().getId()) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_ACCESS);  // 권한이 없으면 예외 발생
        }

        // StutterDetail 조회
        List<StutterDetail> stutterDetails = stutterDetailRepository.findBySimulation(simulation);

        // StutterDetailResponse로 변환하여 반환
        return stutterDetails.stream()
                .map(stutterDetail -> new StutterDetailResponse(
                        stutterDetail.getAudioUrl(),
                        stutterDetail.getWords(),
                        stutterDetail.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    // Simulation 조회 메서드
    private Simulation getSimulation(final long simulationId) {
        return simulationRepository.findById(simulationId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.SIMULATION_NOT_FOUND));
    }

    // 말더듬 분석 결과 저장
    public void saveStutterDetail(long simulationId, String audioFileName) {

        // 권한 확인
        Long doctorId = jwtTokenProvider.getDoctorId();
        Simulation simulation = getSimulation(simulationId);
        if(doctorId != simulation.getPatient().getDoctor().getId()) {
            throw new CustomException(ACCESS_DENIED);
        }

        // 말더듬 분석 요청 및 말더듬 결과 저장
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("audio_name", audioFileName);

        webClient.post()
                .uri("/stutter_model")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.NO_CONTENT)) { // 204
                        log.info("말더듬 분석 결과 없음!");
                        return Mono.empty();
                    } else if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(StutterDetailListResponse.class)
                                .map(StutterDetailListResponse::toStutterDetailResponse)
                                .doOnNext(responseBody -> {
                                    stutterDetailRepository.save(StutterDetail.create(simulation, responseBody));
                                });
                    } else if (response.statusCode().is4xxClientError()) {
                        return Mono.error(new CustomException(STUTTER_CLIENT_ERROR));
                    } else {
                        return Mono.error(new CustomException(STUTTER_SERVER_ERROR));
                    }
                })
                .block();

    }

}
