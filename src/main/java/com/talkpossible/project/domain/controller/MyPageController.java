package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.patient.reponse.PatientListResponse;
import com.talkpossible.project.domain.dto.patient.request.PostPatient;
import com.talkpossible.project.domain.service.DoctorService;
import com.talkpossible.project.domain.service.MyPageService;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MyPageController {

    private final MyPageService myPageService;
    private final JwtTokenProvider jwtTokenProvider;
    private final DoctorService doctorService;

    @GetMapping("/mypage/patients")
    public ResponseEntity<PatientListResponse> getPatientList() {
       return ResponseEntity.ok(myPageService.getPatientList(jwtTokenProvider.getDoctorId()));
    }

    //환자 등록
    @PostMapping("/patients")
    public ResponseEntity<Void> postPatient(@Valid @RequestBody PostPatient postPatient) {
        doctorService.postPatient(postPatient, jwtTokenProvider.getDoctorId());
        return ResponseEntity.ok().build();
    }
}
