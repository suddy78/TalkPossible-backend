package com.talkpossible.project.domain.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talkpossible.project.global.config.type.TokenType;
import com.talkpossible.project.global.exception.CustomException;
import com.talkpossible.project.global.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            // 헤더에서 토큰 추출
            String token = jwtTokenProvider.extractToken((HttpServletRequest) request);

            // 유효한 토큰인지 확인
            if (token != null && jwtTokenProvider.validateToken(token, TokenType.ACCESS_TOKEN)) {
                // 토큰이 유효하면, 토큰에 포함된 정보를 기반으로 Authentication 객체 생성
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // SecurityContext 에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } catch (CustomException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorCode(e.getCustomErrorCode().getCode())
                    .message(e.getMessage())
                    .build();

            httpResponse.setStatus(e.getCustomErrorCode().getHttpStatus().value());
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        }
    }
}
