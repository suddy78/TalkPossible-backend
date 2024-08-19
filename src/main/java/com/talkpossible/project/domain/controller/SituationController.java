package com.talkpossible.project.domain.controller;


import com.talkpossible.project.domain.dto.situation.response.SituationListResponse;
import com.talkpossible.project.domain.service.SituationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SituationController {

    private final SituationService situationService;

    @GetMapping("/situations")
    public ResponseEntity<SituationListResponse> getSituationList() {
        return ResponseEntity.ok(situationService.getSituationList());
    }
}
