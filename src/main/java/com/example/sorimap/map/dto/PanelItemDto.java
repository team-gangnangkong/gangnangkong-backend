package com.example.sorimap.map.dto;

import com.example.sorimap.feed.domain.FeedType;
import com.example.sorimap.feed.domain.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PanelItemDto {
    private Long id;
    private String title;
    private String address;
    private int likes;
    private FeedType type;
    private double lat;
    private double lng;
    private Sentiment sentiment; // ✅ 긍정/부정
}
