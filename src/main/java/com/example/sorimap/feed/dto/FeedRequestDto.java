package com.example.sorimap.feed.dto;

import com.example.sorimap.feed.domain.FeedType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedRequestDto {
    private String title;
    private String content;
    private FeedType type;
    private String address;
    private double lat;
    private double lng;

    // 🔹 장소 검색/선택 연동용 ID (선택 사항)
    private Long locationId;
}
