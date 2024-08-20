package com.talkpossible.project.domain.service;

import com.talkpossible.project.domain.domain.Doctor;
import com.talkpossible.project.domain.dto.doctor.request.LoginRequest;
import com.talkpossible.project.domain.dto.doctor.response.LoginResponse;
import com.talkpossible.project.domain.dto.doctor.request.SignupRequest;
import com.talkpossible.project.domain.dto.doctor.response.ProfileResponse;
import com.talkpossible.project.global.security.jwt.JwtTokenProvider;
import com.talkpossible.project.domain.repository.DoctorRepository;
import com.talkpossible.project.global.enumType.Role;
import com.talkpossible.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.talkpossible.project.global.exception.CustomErrorCode.*;

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
            throw new CustomException(EMAIL_ALREADY_EXISTS);
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
                .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), doctor.getPassword())){
            throw new CustomException(PASSWORD_MISMATCH);
        }

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(doctor.getId(), doctor.getEmail(), doctor.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(doctor.getId());

        // TODO refresh token 업데이트

        return new LoginResponse(accessToken, refreshToken);
    }

    // 사용자 프로필 조회
    public ProfileResponse getProfile() {

        Doctor doctor = doctorRepository.findById(jwtTokenProvider.getDoctorId())
                .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));

        return ProfileResponse.from(doctor.getProfileImgUrl(), doctor.getName());
    }

}
