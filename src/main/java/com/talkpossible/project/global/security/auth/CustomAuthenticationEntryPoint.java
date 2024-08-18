package com.talkpossible.project.global.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talkpossible.project.global.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.talkpossible.project.global.exception.CustomErrorCode.UNAUTHORIZED_ACCESS;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(UNAUTHORIZED_ACCESS.getCode())
                .message(UNAUTHORIZED_ACCESS.getMessage())
                .build();

        response.setStatus(UNAUTHORIZED_ACCESS.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
