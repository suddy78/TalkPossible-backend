package com.talkpossible.project.domain.dto.speechrate.request;

import lombok.Getter;

import java.util.List;

@Getter
public class SpeechRateRequest {
    private List<String> audioFileNameList;
}
