package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.domain.StutterDetail;
import com.talkpossible.project.domain.dto.stutter.response.StutterDetailResponse;
import com.talkpossible.project.domain.repository.SimulationRepository;
import com.talkpossible.project.domain.repository.StutterDetailRepository;
import com.talkpossible.project.global.exception.CustomErrorCode;
import com.talkpossible.project.global.exception.CustomException;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;  // JwtTokenProvider 추가

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
}
