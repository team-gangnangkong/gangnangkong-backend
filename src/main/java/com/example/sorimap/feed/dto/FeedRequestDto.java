package com.example.sorimap.feed.dto;

import com.example.sorimap.feed.domain.FeedType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedRequestDto {
    private String title;
    private String content;
    private String address;
    private double lat;
    private double lng;

    // ✅ 카카오 장소 ID(프론트가 보내줌)
    private String kakaoPlaceId;

    // ❌ 내부 PK는 더 이상 받지 않음 (호환 필요하면 주석만 유지)
    // private Long locationId;
}
