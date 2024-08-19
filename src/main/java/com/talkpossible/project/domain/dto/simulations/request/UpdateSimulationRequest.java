package com.talkpossible.project.domain.dto.simulations.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSimulationRequest {
    private String voiceFileName;
    private String content;
}
