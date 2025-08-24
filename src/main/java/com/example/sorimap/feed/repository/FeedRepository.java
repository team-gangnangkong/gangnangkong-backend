package com.example.sorimap.feed.repository;

import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.feed.domain.FeedStatus;
import com.example.sorimap.feed.domain.FeedType;
import com.example.sorimap.feed.domain.Sentiment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    // 정렬용(공감순)
    List<Feed> findByTypeOrderByLikesDesc(FeedType type, Pageable pageable);
    List<Feed> findAllByOrderByLikesDesc(Pageable pageable);
    List<Feed> findByStatusOrderByLikesDesc(FeedStatus status, Pageable pageable);
    List<Feed> findByTypeAndAddressContainingIgnoreCaseOrderByLikesDesc(FeedType type, String address, Pageable pageable);
    List<Feed> findByAddressContainingIgnoreCaseOrderByLikesDesc(String address, Pageable pageable);

    // 위치 범위 조회
    List<Feed> findByLatBetweenAndLngBetween(double minLat, double maxLat, double minLng, double maxLng);

    // 위치 범위 + 감정 필터
    List<Feed> findByLatBetweenAndLngBetweenAndSentiment(double minLat, double maxLat, double minLng, double maxLng, Sentiment sentiment);

    // 상태 필터
    List<Feed> findByStatus(FeedStatus status);

    // 위치 범위 + 상태 필터
    List<Feed> findByLatBetweenAndLngBetweenAndStatus(double minLat, double maxLat, double minLng, double maxLng, FeedStatus status);

    // ✅ kakaoPlaceId 기반 조회 (JPA Join)
    List<Feed> findByLocation_KakaoPlaceId(String kakaoPlaceId);
    List<Feed> findByStatusAndLocation_KakaoPlaceId(FeedStatus status, String kakaoPlaceId);

    // ✅ 내가 작성한 모든 피드
    List<Feed> findByUserId(Long userId);

    // ✅ 내가 작성한 특정 피드 상세
    Optional<Feed> findByIdAndUserId(Long feedId, Long userId);
}
