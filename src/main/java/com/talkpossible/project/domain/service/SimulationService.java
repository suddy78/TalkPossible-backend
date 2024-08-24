package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Conversation;
import com.talkpossible.project.domain.domain.Patient;
import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.domain.Situation;
import com.talkpossible.project.domain.dto.motion.response.UserMotionListResponse;
import com.talkpossible.project.domain.dto.simulation.response.BasicInfoResponse;
import com.talkpossible.project.domain.dto.simulation.response.PatientSimulationDetailResponse;
import com.talkpossible.project.domain.dto.simulation.response.PatientSimulationListResponse;
import com.talkpossible.project.domain.dto.simulation.response.UserSimulationResponse;
import com.talkpossible.project.domain.repository.*;
import com.talkpossible.project.domain.dto.simulation.request.UpdateSimulationRequest;
import com.talkpossible.project.global.exception.CustomErrorCode;
import com.talkpossible.project.global.exception.CustomException;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.talkpossible.project.global.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final SimulationRepository simulationRepository;
    private final PatientRepository patientRepository;
    private final SituationRepository situationRepository;
    private final MotionDetailRepository motionDetailRepository;
    private final ConversationRepository conversationRepository;
    private final StutterDetailRepository stutterDetailRepository;


    @Transactional
    public UserSimulationResponse createSimulation(Long patientId, Long situationId) {
        // Patient와 Situation 객체를 데이터베이스에서 조회
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.DOCTOR_NOT_FOUND));
        Situation situation = situationRepository.findById(situationId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_VALUE));

        // Simulation 객체 생성
        Simulation simulation = Simulation.builder()
                .patient(patient)
                .situation(situation)
                .build();

        // Simulation 객체를 데이터베이스에 저장
        Simulation savedSimulation = simulationRepository.save(simulation);

        // 생성된 Simulation의 ID를 담아 응답
        return new UserSimulationResponse(savedSimulation.getId());
    }

    @Transactional
    public void addConversation(Long patientId, Long simulationId, UpdateSimulationRequest request) {
        // Patient 객체를 데이터베이스에서 조회
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.DOCTOR_NOT_FOUND));

        // Simulation 객체를 데이터베이스에서 조회
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_VALUE));

        // 새로운 Conversation 객체 생성
        Conversation conversation = Conversation.builder()
                .simulation(simulation)
                .patient(patient)
                .content(request.getContent())
                .sendTime(LocalDateTime.now()) // 현재 시간을 보내는 시간으로 설정
                .build();

        // Conversation 객체를 데이터베이스에 저장
        conversationRepository.save(conversation);
    }
  
    // 피드백 조회 - 시뮬레이션 정보 & 영상
    public BasicInfoResponse getBasicFeedback(long simulationId) {

        Long doctorId = jwtTokenProvider.getDoctorId();
        Simulation simulation = getSimulation(simulationId);

        if(doctorId != simulation.getPatient().getDoctor().getId()){
            throw new CustomException(ACCESS_DENIED);
        }

        // 동작 감지 횟수 조회
        long motionCount = motionDetailRepository.countBySimulationId(simulationId);

        return BasicInfoResponse.from(simulation, simulation.getPatient(), motionCount);
    }

    @Transactional(readOnly = true)
    public UserMotionListResponse getMotionFeedback(final long simulationId) {

        Long doctorId = jwtTokenProvider.getDoctorId();
        Simulation simulation = getSimulation(simulationId);

        if(doctorId != simulation.getPatient().getDoctor().getId()){
            throw new CustomException(ACCESS_DENIED);
        }

        return UserMotionListResponse.of(motionDetailRepository.findAllBySimulationId(simulationId));

    }

    @Transactional(readOnly = true)
    public PatientSimulationListResponse getPatientSimulationInfo(final long patientId) {

        List<Simulation> patientSimulations = simulationRepository.findAllByPatientId(patientId);

        List<PatientSimulationDetailResponse> patientSimulationDetailResponses = patientSimulations.stream()
                .map(simulation ->
                        PatientSimulationDetailResponse.of(
                                simulation,
                                stutterDetailRepository.findBySimulation(simulation).size(),
                                motionDetailRepository.findAllBySimulation(simulation).size()
                        )
                ).toList();

        return PatientSimulationListResponse.of(getPatient(patientId).getName(), patientSimulationDetailResponses);

    }

    private Simulation getSimulation(final long simulationId) {
        return simulationRepository.findById(simulationId)
                .orElseThrow(() -> new CustomException(SIMULATION_NOT_FOUND));
    }

    private Patient getPatient(final long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(PATIENT_NOT_FOUND));
    }

}
