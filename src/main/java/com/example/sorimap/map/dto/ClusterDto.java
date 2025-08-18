package com.example.sorimap.map.dto;

import com.example.sorimap.feed.domain.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClusterDto {
    private double lat;
    private double lng;
    private long count;
    private Sentiment sentiment;
}
