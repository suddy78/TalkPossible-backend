package com.talkpossible.project.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode {

    // Common (1xxx)
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "서버 내부에 오류가 있습니다."),
    INVALID_VALUE(BAD_REQUEST, 1002, "잘못된 입력값입니다."),

    // 사용자 인증 (2xxx)
    EMAIL_ALREADY_EXISTS(CONFLICT, 2001, "이미 가입된 이메일입니다."),
    PASSWORD_MISMATCH(UNAUTHORIZED, 2002, "비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_ACCESS(UNAUTHORIZED, 2003, "인증된 사용자만 접근 가능한 URL입니다."),
    TOKEN_EXPIRED(UNAUTHORIZED, 2004, "토큰이 만료되었습니다."),
    INVALID_TOKEN_FORMAT(BAD_REQUEST, 2005, "토큰이 비어있거나 잘못된 형식입니다."),
    INVALID_TOKEN(UNAUTHORIZED, 2006, "토큰이 유효하지 않습니다."),
    TOKEN_VALIDATION_ERROR(INTERNAL_SERVER_ERROR, 2007, "토큰 검증 중 오류가 발생했습니다."),
    ACCESS_DENIED(FORBIDDEN, 2008, "접근 권한이 없습니다."),
    AUTHENTICATION_NOT_FOUND(UNAUTHORIZED, 2009, "인증 정보를 찾을 수 없습니다."),

    // Doctor (3xxx)
    DOCTOR_NOT_FOUND(NOT_FOUND, 3001, "해당하는 의사 정보를 찾을 수 없습니다."),

    // Simulation (4xxx)
    SIMULATION_NOT_FOUND(NOT_FOUND, 4001, "해당하는 시뮬레이션 정보를 찾을 수 없습니다."),

    // Patient (5xxx)
    PATIENT_NOT_FOUND(NOT_FOUND, 5001, "해당하는 환자 정보를 찾을 수 없습니다."),

    // Voice Analysis (6xxx)
    STUTTER_CLIENT_ERROR(BAD_REQUEST, 6001, "말더듬 분석 중 오류가 발생했습니다. (Client Error)"),
    STUTTER_SERVER_ERROR(INTERNAL_SERVER_ERROR, 6002, "말더듬 분석 중 오류가 발생했습니다. (Server Error)"),
    SPEECH_RATE_CLIENT_ERROR(BAD_REQUEST, 6003, "발화속도 측정 중 오류가 발생했습니다. (Client Error)"),
    SPEECH_RATE_SERVER_ERROR(INTERNAL_SERVER_ERROR, 6004, "발화속도 측정 중 오류가 발생했습니다. (Server Error)"),
    FILLER_WORD_CLIENT_ERROR(BAD_REQUEST, 6005, "추임색 분석 중 오류가 발생했습니다. (Client Error)"),
    FILLER_WORD_SERVER_ERROR(INTERNAL_SERVER_ERROR, 6006, "추임색 분석 중 오류가 발생했습니다. (Server Error)"),

    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

}
