package com.example.sorimap.search.repository;

import com.example.sorimap.search.domain.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    // 일반/초성 검색 (DB 검색을 계속 쓴다면 유지)
    List<LocationEntity> findByNameContaining(String name);
    List<LocationEntity> findByInitialsContaining(String initials);

    // ✅ kakaoPlaceId로 조회
    Optional<LocationEntity> findByKakaoPlaceId(String kakaoPlaceId);
}
