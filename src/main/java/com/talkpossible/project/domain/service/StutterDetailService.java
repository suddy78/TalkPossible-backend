package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.domain.StutterDetail;
import com.talkpossible.project.domain.dto.stutter.response.StutterDetailResponse;
import com.talkpossible.project.domain.repository.SimulationRepository;
import com.talkpossible.project.domain.repository.StutterDetailRepository;
import com.talkpossible.project.global.exception.CustomErrorCode;
import com.talkpossible.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class StutterDetailService {

    private final SimulationRepository simulationRepository;
    private final StutterDetailRepository stutterDetailRepository;

    @Transactional(readOnly = true)
    public List<StutterDetailResponse> getStutterDetails(Long simulationId) {
        // Simulation 존재 여부 확인
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_VALUE)); // 예외 처리

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
}
