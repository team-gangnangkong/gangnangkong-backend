package com.example.sorimap.feed.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedType type; // 민원 / 문화

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment; // 긍정 / 부정 / 중립

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedStatus status = FeedStatus.OPEN; // 기본값: 미해결

    private String address;
    private Double lat;
    private Double lng;

    // 🔹 LocationEntity FK 대신 ID만 저장
    @Column(name = "location_id")
    private Long locationId;

    @Builder.Default
    @Column(nullable = false)
    private int likes = 0;

    @Column(columnDefinition = "TEXT") // 여러 장 URL 콤마 저장
    private String imageUrls;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // ✅ User 연관관계 대신 userId 직접 저장
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
