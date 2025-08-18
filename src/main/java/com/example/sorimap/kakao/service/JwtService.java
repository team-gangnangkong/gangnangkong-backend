package com.example.sorimap.kakao.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKeyBase64;

    @Value("${jwt.token-validity-in-minutes}")
    private long tokenValidityInMinutes; // Access Token 유효기간(분)

    @Value("${jwt.refresh-token-validity-in-minutes}")
    private long refreshTokenValidityInMinutes; // Refresh Token 유효기간(분)

    private Key key;
    private long accessTokenExpiryMs;
    private long refreshTokenExpiryMs;

    @PostConstruct
    public void init() {
        // Base64 디코딩 기반 키 생성
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.accessTokenExpiryMs = tokenValidityInMinutes * 60_000L;
        this.refreshTokenExpiryMs = refreshTokenValidityInMinutes * 60_000L;
    }

    /** Access Token 생성 (subject = userId) */
    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiryMs);
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Refresh Token 생성 */
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiryMs);
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** Claims(subject 포함) 파싱 */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** subject 추출 (userId 문자열로 저장된 값) */
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    /** userId(Long) 추출 */
    public Long getUserIdFromToken(String token) {
        if (!validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        return Long.parseLong(getSubject(token));
    }

    /** 컨트롤러 편의용: 토큰 null/유효성 검증 후 userId 반환 */
    public Long requireUserId(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return getUserIdFromToken(token);
    }
}
