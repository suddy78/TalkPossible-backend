package com.talkpossible.project.domain.login;

import com.talkpossible.project.domain.login.dto.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class DoctorController {

    private final DoctorService doctorService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest){
        doctorService.signup(signupRequest);
        return ResponseEntity.ok().build();
    }

}
