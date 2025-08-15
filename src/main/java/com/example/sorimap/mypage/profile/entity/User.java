package com.example.sorimap.mypage.profile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", indexes = @Index(columnList = "nickname", unique = true))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 카카오 ID 기반 서비스용 유니크 ID

    @Column(nullable = false, length = 10, unique = true)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Version
    private Long version;

}
