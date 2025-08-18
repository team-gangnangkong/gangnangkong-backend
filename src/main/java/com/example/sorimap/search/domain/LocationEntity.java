package com.example.sorimap.search.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "location")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String initials; // 초성
    private String name; // 장소 이름
    private String address; // 주소
    private double latitude; // 위도
    private double longitude; // 경도
}