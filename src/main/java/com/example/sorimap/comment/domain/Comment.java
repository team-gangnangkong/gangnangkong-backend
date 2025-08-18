package com.example.sorimap.comment.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Feed 연관관계 대신 feedId만 저장
    @Column(nullable = false)
    private Long feedId;

    // 🔹 User 연관관계 대신 userId만 저장 (KakaoUser PK)
    @Column(nullable = false)
    private Long userId;

    @Column(length = 1000, nullable = false)
    private String body;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
