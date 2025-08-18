package com.example.sorimap.search.repository;

import com.example.sorimap.search.domain.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    // 일반 검색
    List<LocationEntity> findByNameContaining(String name);

    // 초성 검색
    List<LocationEntity> findByInitialsContaining(String initials);
}
