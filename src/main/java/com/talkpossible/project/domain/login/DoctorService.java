package com.talkpossible.project.domain.login;

import com.talkpossible.project.domain.chatGPT.domain.Doctor;
import com.talkpossible.project.domain.login.dto.LoginRequest;
import com.talkpossible.project.domain.login.dto.LoginResponse;
import com.talkpossible.project.domain.login.dto.SignupRequest;
import com.talkpossible.project.global.config.type.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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

    // 로그인
    public LoginResponse login(LoginRequest loginRequest) {

        // 이메일 조회
        Doctor doctor = doctorRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 사용자입니다."));
                //.orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), doctor.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            //throw new CustomException(LOGIN_FAILED);
        }

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(doctor.getId(), doctor.getEmail(), doctor.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(doctor.getId());

        return new LoginResponse(accessToken, refreshToken);
    }

}
