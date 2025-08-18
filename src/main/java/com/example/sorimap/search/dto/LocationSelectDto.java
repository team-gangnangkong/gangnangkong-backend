package com.example.sorimap.search.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationSelectDto {
    private Long id;         // 장소 ID
    private String name;     // 장소명
    private String address;  // 주소
    private Double latitude; // 위도
    private Double longitude;// 경도
}
