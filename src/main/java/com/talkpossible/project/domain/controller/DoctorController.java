package com.talkpossible.project.domain.controller;

import com.talkpossible.project.domain.dto.doctor.request.LoginRequest;
import com.talkpossible.project.domain.dto.doctor.response.LoginResponse;
import com.talkpossible.project.domain.dto.doctor.request.SignupRequest;
import com.talkpossible.project.domain.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class DoctorController {

    private final DoctorService doctorService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest){
        doctorService.signup(signupRequest);
        return ResponseEntity.ok().build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(doctorService.login(loginRequest));
    }

    // (테스트용) 인증이 필수인 URL 요청
    @GetMapping("/login-required-test")
    public ResponseEntity<String> loginRequiredTest(){
        return ResponseEntity.ok("login-required-test success");
    }

    // (테스트용) 인증이 필수가 아닌 URL 요청
    @GetMapping("/login-not-required-test")
    public ResponseEntity<String> loginNotRequiredTest(){
        return ResponseEntity.ok("login-not-required-test success");
    }

}
