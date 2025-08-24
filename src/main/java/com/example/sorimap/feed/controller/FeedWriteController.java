package com.example.sorimap.feed.controller;

import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.feed.dto.FeedRequestDto;
import com.example.sorimap.feed.service.FeedWriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedWriteController {

    private final FeedWriteService feedWriteService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱용

    /**
     * 피드 작성 (제목, 내용, kakaoPlaceId + 다중 이미지 업로드)
     * 프론트에서 multipart/form-data 로 전송:
     *   - feed: JSON (title, content, kakaoPlaceId, lat, lng, address)
     *   - images: 파일 배열 (선택)
     */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createFeed(HttpServletRequest request,
                                        @RequestParam("feed") String feedJson,
                                        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {

        // JSON 문자열 → DTO 변환
        FeedRequestDto dto = objectMapper.readValue(feedJson, FeedRequestDto.class);

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Feed feed = feedWriteService.createFeed(dto, userId, images);
        return ResponseEntity.ok(feed.getId());
    }
}
