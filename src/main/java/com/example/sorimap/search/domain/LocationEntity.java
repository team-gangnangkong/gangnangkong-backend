package com.example.sorimap.search.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(
        name = "location",
        uniqueConstraints = @UniqueConstraint(name = "uk_location_kakao_place", columnNames = "kakao_place_id")
)
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_place_id", nullable = false, length = 64)
    private String kakaoPlaceId;   // ✅ getKakaoPlaceId()/setKakaoPlaceId()

    private String initials;       // (검색용)

    @Column(nullable = false)
    private String name;           // ✅ getName()/setName()

    @Column(nullable = false)
    private String address;        // ✅ getAddress()/setAddress()

    @Column(nullable = false)
    private double latitude;       // ✅ getLatitude()/setLatitude()

    @Column(nullable = false)
    private double longitude;      // ✅ getLongitude()/setLongitude()
}
