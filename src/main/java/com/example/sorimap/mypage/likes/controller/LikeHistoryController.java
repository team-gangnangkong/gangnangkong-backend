package com.example.sorimap.mypage.likes.controller;

import com.example.sorimap.mypage.likes.dto.LikeHistoryResponse;
import com.example.sorimap.mypage.likes.service.LikeHistoryService;
import com.example.sorimap.mypage.profile.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeHistoryController {

    private final LikeHistoryService likeHistoryService;

    @GetMapping("/history")
    public ResponseEntity<List<LikeHistoryResponse>> getLikeHistory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long userId = userDetails.getId();
        List<LikeHistoryResponse> history = likeHistoryService.getWeeklyLikeHistory(userId);
        return ResponseEntity.ok(history);
    }
}
