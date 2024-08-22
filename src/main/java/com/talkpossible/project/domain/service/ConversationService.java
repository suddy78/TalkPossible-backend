package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.dto.conversation.response.ConversationResponse;
import com.talkpossible.project.domain.repository.ConversationRepository;
import com.talkpossible.project.domain.repository.SimulationRepository;
import com.talkpossible.project.global.exception.CustomException;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.talkpossible.project.global.exception.CustomErrorCode.ACCESS_DENIED;
import static com.talkpossible.project.global.exception.CustomErrorCode.SIMULATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final ConversationRepository conversationRepository;
    private final SimulationRepository simulationRepository;

    // 피드백 조회 - 대화 내용
    public ConversationResponse getConversationFeedback(long simulationId) {

        // 권한 확인
        Long doctorId = jwtTokenProvider.getDoctorId();
        Simulation simulation = getSimulation(simulationId);

        if(doctorId != simulation.getPatient().getDoctor().getId()){
            throw new CustomException(ACCESS_DENIED);
        }

        // 대화 내역 조회
        List<ConversationResponse.Message> messageList =
                conversationRepository.findBySimulationIdOrderBySendTimeAsc(simulationId)
                    .stream()
                    .map(ConversationResponse.Message::from)
                    .collect(Collectors.toList());

        return ConversationResponse.from(messageList);
    }

    private Simulation getSimulation(final long simulationId) {
        return simulationRepository.findById(simulationId)
                .orElseThrow(() -> new CustomException(SIMULATION_NOT_FOUND));
    }
}
