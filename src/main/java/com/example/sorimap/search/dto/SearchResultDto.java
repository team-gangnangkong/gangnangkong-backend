package com.example.sorimap.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultDto {

    private Long id;        // 위치 ID
    private String name;    // 위치 이름
    private String address; // 주소
    private double latitude;  // 위도
    private double longitude; // 경도
}
