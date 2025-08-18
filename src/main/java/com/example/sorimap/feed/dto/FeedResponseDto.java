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
    private String address;
    private Double lat;
    private Double lng;

    private Long locationId;

    private int likes;
    private List<String> imageUrls;
    private String userNickname;   // ✅ 작성자 닉네임
    private LocalDateTime createdAt;
}
