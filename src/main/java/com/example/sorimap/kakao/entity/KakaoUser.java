package com.example.sorimap.kakao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class KakaoUser {

    @Id
    @Column(nullable = false, unique = true)
    private Long kakaoId;

    // 닉네임
    @Column(nullable = false)
    private String nickname;

    // 프로필 이미지 URL
    private String profileImageUrl;

    // 연결된 시간 (카카오 로그인)
    private LocalDateTime connectedAt;

    // 기본 생성자
    public KakaoUser() {
    }

    // 생성자
    public KakaoUser(Long kakaoId, String nickname, String profileImageUrl, LocalDateTime connectedAt) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.connectedAt = connectedAt;
    }
}
