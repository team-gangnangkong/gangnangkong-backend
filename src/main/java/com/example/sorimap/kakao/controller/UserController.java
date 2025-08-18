package com.example.sorimap.kakao.controller;

import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.repository.KakaoUserRepository;
import com.example.sorimap.kakao.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class

UserController {

    private final JwtService jwtService;
    private final KakaoUserRepository kakaoUserRepository;

    /**
     * 현재 로그인한 유저 정보 조회 (쿠키 기반)
     * - ACCESS-TOKEN(HttpOnly) 쿠키를 검증해서 kakaoId를 꺼내고 DB 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(value = "ACCESS-TOKEN", required = false) String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: no ACCESS-TOKEN cookie");
        }

        try {
            if (!jwtService.validateToken(accessToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: invalid token");
            }

            String kakaoIdStr = jwtService.getSubject(accessToken); // subject에 kakaoId 저장했다고 가정
            Long kakaoId = Long.parseLong(kakaoIdStr);

            KakaoUser user = kakaoUserRepository.findById(kakaoId).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Map<String, Object> resp = new HashMap<>();
            resp.put("id", user.getKakaoId());
            resp.put("nickname", user.getNickname());
            resp.put("profileImageUrl", user.getProfileImageUrl());
            resp.put("loginType", "KAKAO");

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("GET /api/user/me failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }

    /**
     * 로그아웃 (쿠키 삭제)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie accessClear = ResponseCookie.from("ACCESS-TOKEN", "")
                .httpOnly(true)
                .secure(false) // 운영 배포 시 true
                .sameSite("Lax")
                .path("/")
                .maxAge(0) // 즉시 삭제
                .build();

        ResponseCookie refreshClear = ResponseCookie.from("REFRESH-TOKEN", "")
                .httpOnly(true)
                .secure(false) // 운영 배포 시 true
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", accessClear.toString())
                .header("Set-Cookie", refreshClear.toString())
                .body("ok");
    }
}
