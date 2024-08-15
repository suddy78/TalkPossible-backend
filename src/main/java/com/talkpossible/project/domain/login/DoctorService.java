package com.talkpossible.project.domain.login;

import com.talkpossible.project.domain.chatGPT.domain.Doctor;
import com.talkpossible.project.domain.login.dto.SignupRequest;
import com.talkpossible.project.global.config.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(SignupRequest signupRequest) {

        // 이메일 가입 여부 확인
        doctorRepository.findByEmail(signupRequest.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
            //throw new CustomException(EMAIL_ALREADY_EXISTS);
        });

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // Doctor 등록
        doctorRepository.save(Doctor.create(signupRequest, encryptedPassword, Role.USER));

    }

}
