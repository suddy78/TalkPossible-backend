package com.talkpossible.project.domain.login;

import com.talkpossible.project.global.config.type.Role;
import com.talkpossible.project.global.config.type.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

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
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidTime = accessTokenValidTime.toMillis();
        this.refreshTokenValidTime = refreshTokenValidTime.toMillis();
        this.customUserDetailsService = customUserDetailsService;
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
            log.warn(tokenType + ": 토큰이 만료되었습니다.", e);
        } catch (IllegalArgumentException e) {
            log.warn(tokenType + ": 토큰이 비어있거나 잘못된 형식입니다.", e);
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | UnsupportedJwtException e) {
//            log.error(tokenType + ": 토큰이 유효하지 않습니다.", e);
        } catch (io.jsonwebtoken.security.SecurityException e) {
            log.error(tokenType + ": 토큰의 서명이 유효하지 않습니다.", e);
        } catch (MalformedJwtException e){
            log.error(tokenType + ": 토큰의 구조가 올바르지 않습니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error(tokenType + ": 서버에서 지원하지 않는 토큰 형식입니다.", e);
        } catch (Exception e) {
            log.info(tokenType + ": 토큰 검증 중 오류가 발생했습니다.", e);
        }

        return false;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String accessToken) {

        String email = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(accessToken)
                            .getBody()
                            .get("email", String.class);
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
    }

}
