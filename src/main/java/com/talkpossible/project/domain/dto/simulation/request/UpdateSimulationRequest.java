package com.talkpossible.project.domain.dto.simulation.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSimulationRequest {
    private String content;

    @JsonProperty("vName")
    private String vName;
}
