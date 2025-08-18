package com.example.sorimap.reaction.domain;

import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.kakao.entity.KakaoUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"feed_id", "user_id"}))
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ Feed 연관관계
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ KakaoUser 연관관계
    @JoinColumn(name = "user_id", nullable = false)
    private KakaoUser user;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    // ✅ 생성 시 자동으로 현재 시간 저장
    @CreationTimestamp
    private LocalDateTime createdAt;

    /** ✅ Hibernate가 createdAt을 넣지 못했을 경우 대비 */
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
