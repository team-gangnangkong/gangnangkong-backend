package com.example.sorimap.feed.controller;

import com.example.sorimap.feed.domain.FeedStatus;
import com.example.sorimap.feed.dto.FeedResponseDto;
import com.example.sorimap.feed.dto.MyFeedDetailResponseDto;
import com.example.sorimap.feed.dto.MyFeedResponseDto;
import com.example.sorimap.feed.service.FeedReadService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedReadController {

    private final FeedReadService feedReadService;

    /** 전체 커뮤니티 조회 (kakaoPlaceId 선택 필터) */
    @GetMapping
    public ResponseEntity<List<FeedResponseDto>> getAllFeeds(
            @RequestParam(required = false) String kakaoPlaceId
    ) {
        return ResponseEntity.ok(feedReadService.getAllFeeds(kakaoPlaceId));
    }

    /** 상태별 조회 (예: /api/feeds/status/OPEN?kakaoPlaceId=123456) */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FeedResponseDto>> getFeedsByStatus(
            @PathVariable FeedStatus status,
            @RequestParam(required = false) String kakaoPlaceId
    ) {
        return ResponseEntity.ok(feedReadService.getFeedsByStatus(status, kakaoPlaceId));
    }

    /** 단건 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<FeedResponseDto> getFeedById(@PathVariable Long id) {
        return ResponseEntity.ok(feedReadService.getFeedById(id));
    }

    /** 내가 작성한 민원 목록 조회 */
    @GetMapping("/my")
    public ResponseEntity<List<MyFeedResponseDto>> getMyFeeds(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(feedReadService.getMyFeeds(userId));
    }

    /** 내가 작성한 민원 상세 조회 */
    @GetMapping("/my/{feedId}")
    public ResponseEntity<MyFeedDetailResponseDto> getMyFeedDetail(
            HttpServletRequest request,
            @PathVariable Long feedId
    ) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(feedReadService.getMyFeedDetail(userId, feedId));
    }
}
