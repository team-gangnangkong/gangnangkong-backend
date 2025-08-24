package com.example.sorimap.search.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LocationSelectDto {
    private String kakaoPlaceId;     // ✅ 새 필드 (필수)
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

    // ❌ private Long id;  // 프론트와 주고받지 않음
}
