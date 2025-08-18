package com.example.sorimap.kakao.repository;

import com.example.sorimap.kakao.entity.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoUserRepository extends JpaRepository<KakaoUser, Long> {

    Optional<KakaoUser> findByKakaoId(Long kakaoId);

    boolean existsByNickname(String nickname);

    void deleteByKakaoId(Long kakaoId);
}
