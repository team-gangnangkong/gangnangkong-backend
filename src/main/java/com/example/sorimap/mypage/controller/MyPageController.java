package com.example.sorimap.mypage.controller;

import com.example.sorimap.mypage.dto.MyPageResponse;
import com.example.sorimap.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /** 마이페이지 전체 조회 */
    @GetMapping
    public ResponseEntity<MyPageResponse> getMyPage(HttpServletRequest request) {
        Long kakaoId = (Long) request.getAttribute("userId"); // ✅ JWT subject = kakaoId
        if (kakaoId == null) {
            throw new IllegalStateException("로그인 사용자 정보를 가져올 수 없습니다. (kakaoId is null)");
        }
        return ResponseEntity.ok(myPageService.getMyPage(kakaoId));
    }
}
