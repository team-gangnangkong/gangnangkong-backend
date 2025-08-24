package com.example.sorimap.feed.domain;

import com.example.sorimap.search.domain.LocationEntity;
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
    private FeedType type; // MINWON / MUNHWA

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment; // POSITIVE / NEGATIVE / NEUTRAL

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedStatus status = FeedStatus.OPEN;

    private String address;
    private Double lat;
    private Double lng;

    // ✅ LocationEntity FK 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @Builder.Default
    @Column(nullable = false)
    private int likes = 0;

    @Column(columnDefinition = "TEXT")
    private String imageUrls;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // ✅ User 연관관계 대신 userId 직접 저장
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
