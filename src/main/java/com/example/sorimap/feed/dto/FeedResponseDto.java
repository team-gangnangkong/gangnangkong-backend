package com.example.sorimap.feed.dto;

import com.example.sorimap.feed.domain.FeedStatus;
import com.example.sorimap.feed.domain.FeedType;
import com.example.sorimap.feed.domain.Sentiment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FeedResponseDto {
    private Long id;
    private String title;
    private String content;
    private FeedType type;
    private Sentiment sentiment;
    private FeedStatus status;

    private String address;   // 전체 주소
    private String district;  // ✅ address에서 추출한 구 (예: 수정구, 중원구, 분당구)

    private Double lat;
    private Double lng;

    // ✅ 장소 식별자는 kakaoPlaceId로 제공
    private String kakaoPlaceId;

    private int likes;
    private List<String> imageUrls;
    private String userNickname;
    private LocalDateTime createdAt;
}
