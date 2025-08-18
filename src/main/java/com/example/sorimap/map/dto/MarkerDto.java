package com.example.sorimap.map.dto;

import com.example.sorimap.feed.domain.FeedType;
import com.example.sorimap.feed.domain.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarkerDto {
    private Long id;
    private double lat;
    private double lng;
    private FeedType type;       // MINWON / MUNHWA
    private Sentiment sentiment; // POSITIVE / NEGATIVE
    private int likes;
    private String color;        // HEX 색상 코드
}
