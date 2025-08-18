package com.example.sorimap.reaction.controller;

import com.example.sorimap.reaction.service.ReactionService;
import com.example.sorimap.kakao.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
@Slf4j
public class ReactionController {

    private final ReactionService reactionService;
    private final JwtService jwtService;

    /**
     * 공감(좋아요) 추가
     * - feedId는 요청 파라미터에서 받고
     * - userId는 쿠키에 있는 JWT 토큰에서 꺼냄
     */
    @PostMapping("/like")
    public String likeFeed(@RequestParam Long feedId,
                           @CookieValue(value = "ACCESS-TOKEN", required = false) String accessToken) {

        if (accessToken == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // JWT에서 userId 꺼내기
        Long userId = jwtService.getUserIdFromToken(accessToken);

        log.info("공감 요청: feedId={}, userId={}", feedId, userId);

        return reactionService.likeFeed(feedId, userId);
    }
}
