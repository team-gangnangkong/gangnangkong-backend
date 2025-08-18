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

            // 4. 쿠키에 저장
            ResponseCookie accessCookie = ResponseCookie.from("ACCESS-TOKEN", accessToken)
                    .httpOnly(true)
                    .secure(false) // TODO: 운영 HTTPS 배포 시 true로 변경
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(60 * 60) // 1시간
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("REFRESH-TOKEN", refreshToken)
                    .httpOnly(true)
                    .secure(false) // TODO: 운영 HTTPS 배포 시 true로 변경
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(60 * 60 * 24) // 24시간
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            // 5. 프론트에 내려줄 데이터
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("nickname", kakaoUser.getNickname());

            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            log.error("카카오 로그인 실패", e);
            return ResponseEntity.status(500).body("카카오 로그인 실패: " + e.getMessage());
        }
    }
}
