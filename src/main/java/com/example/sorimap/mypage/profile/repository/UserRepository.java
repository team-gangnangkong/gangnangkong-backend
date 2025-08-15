package com.example.sorimap.mypage.profile.repository;

import com.example.sorimap.mypage.profile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);

    // ✅ 존재 여부 확인용 메서드
    boolean existsByNickname(String nickname);
}
