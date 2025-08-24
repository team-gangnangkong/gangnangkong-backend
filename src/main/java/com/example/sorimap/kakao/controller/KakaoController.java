package com.example.sorimap.kakao.controller;

import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.service.KakaoService;
import com.example.sorimap.kakao.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoService kakaoService;
    private final JwtService jwtService;

    public KakaoController(KakaoService kakaoService, JwtService jwtService) {
        this.kakaoService = kakaoService;
        this.jwtService = jwtService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            log.info("카카오 인가 코드 수신: {}", code);

            // 1. 인가 코드로 카카오 Access Token 발급
            String kakaoAccessToken = kakaoService.kakaoGetAccessViaCode(code);
            log.info("카카오 Access Token 발급 완료");

            // 2. 토큰으로 카카오 사용자 정보 조회
            KakaoUser kakaoUser = kakaoService.kakaoGetUserInfoViaAccessToken(kakaoAccessToken);
            log.info("카카오 사용자 정보 조회 완료: {}", kakaoUser.getNickname());

            // 3. JWT Access / Refresh 발급
            String accessToken = jwtService.createAccessToken(kakaoUser.getKakaoId());
            String refreshToken = jwtService.createRefreshToken(kakaoUser.getKakaoId());

            // 4. 쿠키에 저장 (운영 환경: HTTPS + SameSite=None)
            ResponseCookie accessCookie = ResponseCookie.from("ACCESS-TOKEN", accessToken)
                    .httpOnly(true)
                    .secure(true)             // ✅ HTTPS 필수
                    .sameSite("None")         // ✅ 크로스사이트 허용
                    //.domain("sorimap.it.com") // 🔥 일단 주석 (도메인 강제 시 쿠키 저장 안 될 수 있음)
                    .path("/")
                    .maxAge(60 * 60)          // 1시간
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("REFRESH-TOKEN", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    //.domain("sorimap.it.com")
                    .path("/")
                    .maxAge(60 * 60 * 24 * 14) // 14일
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

// 5. 프론트로 리다이렉트 (JSON 응답 대신 프론트 주소로 보내기)
            response.sendRedirect("https://gangnangkong.netlify.app");

            return null; // sendRedirect로 응답했으므로 body 없음


        } catch (Exception e) {
            log.error("카카오 로그인 실패", e);
            return ResponseEntity.status(500).body("카카오 로그인 실패: " + e.getMessage());
        }
    }

}
