package com.talkpossible.project.global.security.jwt;

import com.talkpossible.project.global.enumType.Role;
import com.talkpossible.project.global.enumType.TokenType;
import com.talkpossible.project.global.exception.CustomErrorCode;
import com.talkpossible.project.global.exception.CustomException;
import com.talkpossible.project.global.security.auth.CustomUserDetails;
import com.talkpossible.project.global.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

import static com.talkpossible.project.global.exception.CustomErrorCode.*;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenValidTime;
    private final long refreshTokenValidTime;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access_token_valid_time}") Duration accessTokenValidTime,
            @Value("${jwt.refresh_token_valid_time}") Duration refreshTokenValidTime,
            CustomUserDetailsService customUserDetailsService) {
        this.key = createKeyFromSecret(secretKey);
        this.accessTokenValidTime = accessTokenValidTime.toMillis();
        this.refreshTokenValidTime = refreshTokenValidTime.toMillis();
        this.customUserDetailsService = customUserDetailsService;
    }

    private Key createKeyFromSecret(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // access token 생성
    public String createAccessToken(Long userPk, String email, Role role) {

        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("role", role);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + accessTokenValidTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userPk))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // refresh token 생성
    public String createRefreshToken(Long userPk){

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + refreshTokenValidTime);

        return Jwts.builder()
                .setSubject(String.valueOf(userPk))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Request Header 에서 토큰값 추출
    public String extractToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JWT 토큰 검증
    public boolean validateToken(String token, TokenType tokenType){

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            handleTokenException(e, TOKEN_EXPIRED, "토큰이 만료되었습니다", tokenType);
        } catch (IllegalArgumentException e) {
            handleTokenException(e, INVALID_TOKEN_FORMAT, "토큰이 비어있거나 잘못된 형식입니다", tokenType);
        } catch (io.jsonwebtoken.security.SecurityException e) {
            handleTokenException(e, INVALID_TOKEN, "토큰의 서명이 유효하지 않습니다", tokenType);
        } catch (MalformedJwtException e) {
            handleTokenException(e, INVALID_TOKEN, "토큰의 구조가 올바르지 않습니다", tokenType);
        } catch (UnsupportedJwtException e) {
            handleTokenException(e, INVALID_TOKEN, "서버에서 지원하지 않는 토큰 형식입니다", tokenType);
        } catch (Exception e) {
            handleTokenException(e, TOKEN_VALIDATION_ERROR, "토큰 검증 중 오류가 발생했습니다", tokenType);
        }

        return false;
    }

    // 토큰 유효성 검증 관련 예외 처리
    private void handleTokenException(Exception e, CustomErrorCode errorCode,
                                      String errorMessage, TokenType tokenType) {
        String message = String.format("%s: %s", tokenType, errorMessage);
        log.error(message, e);
        throw new CustomException(errorCode, message);
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {

        CustomUserDetails customUserDetails =
                (CustomUserDetails) customUserDetailsService.loadUserByUsername(getEmailFromToken(token));

        return new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
    }

    private String getEmailFromToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    private Long getIdFromToken(String token) {

        return Long.parseLong(
                Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
    }

    // * 로그인이 필수인 엔드포인트에서 사용
    // SecurityContextHolder 를 통해 doctorId를 가져옴
    public Long getDoctorId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        return null;
    }

    // * 로그인이 필수가 아닌 엔드포인트에서 사용
    // SecurityContextHolder 또는 HttpServletRequest 를 통해 doctorId를 가져옴
    public Long getDoctorIdByServlet(HttpServletRequest request) {

        // 먼저 SecurityContextHolder 에서 doctorId 확인
        Long doctorId = getDoctorId();
        if (doctorId != null) {
            return doctorId;
        }

        // SecurityContextHolder 에 정보가 없을 경우, HttpServletRequest 에서 토큰 확인
        String token = extractToken(request);
        if (token != null && validateToken(token, TokenType.ACCESS_TOKEN)) {
            return getIdFromToken(token);
        }

        return null;
    }

}
