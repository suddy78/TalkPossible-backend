package com.talkpossible.project.domain.dto.simulation.response;

import java.util.List;

public record PatientSimulationListResponse(
        String name,
        List<PatientSimulationDetailResponse> simulations
) {
    public static PatientSimulationListResponse of(String name, List<PatientSimulationDetailResponse> simulations) {
        return new PatientSimulationListResponse(name, simulations);
    }
}
