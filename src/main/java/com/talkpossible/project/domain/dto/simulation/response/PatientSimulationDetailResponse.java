package com.talkpossible.project.domain.dto.simulation.response;

import com.talkpossible.project.domain.domain.MotionDetail;
import com.talkpossible.project.domain.domain.Simulation;
import com.talkpossible.project.domain.domain.StutterDetail;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record PatientSimulationDetailResponse(
        long simulationId,
        String date,
        String situation,
        String totalTime,
        float wordsPerMin,
        int stutterCount,
        int motionCount
) {
    public static PatientSimulationDetailResponse of(
            final Simulation simulation,
            final int stutterCount,
            final int motionCount
    ) {
        return PatientSimulationDetailResponse.builder()
                .simulationId(simulation.getId())
                .date(String.valueOf(simulation.getRunDate()))
                .situation(simulation.getSituation().getTitle())
                .totalTime(String.valueOf(simulation.getTotalTime()))
                .wordsPerMin(simulation.getWordsPerMin())
                .stutterCount(stutterCount)
                .motionCount(motionCount)
                .build();
    }
}
