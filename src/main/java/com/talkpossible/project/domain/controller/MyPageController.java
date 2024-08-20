package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.patient.reponse.PatientListResponse;
import com.talkpossible.project.domain.service.MyPageService;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MyPageController {

    private final MyPageService myPageService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/mypage/patients")
    public ResponseEntity<PatientListResponse> getPatientList() {
       return ResponseEntity.ok(myPageService.getPatientList(jwtTokenProvider.getDoctorId()));
    }
}
