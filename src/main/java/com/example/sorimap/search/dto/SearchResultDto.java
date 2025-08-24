package com.example.sorimap.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SearchResultDto {
    private String kakaoPlaceId; // ✅
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    // ❌ private Long id;
}
