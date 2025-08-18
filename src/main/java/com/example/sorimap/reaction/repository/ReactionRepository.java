package com.example.sorimap.reaction.repository;

import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.reaction.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    // ✅ 특정 피드 + 특정 유저 (중복 공감 방지용)
    Optional<Reaction> findByFeedAndUser(Feed feed, KakaoUser user);

    // ✅ 특정 유저의 모든 공감 내역
    List<Reaction> findByUser(KakaoUser user);

    // ✅ 특정 피드에 달린 공감 전체 조회
    List<Reaction> findByFeed(Feed feed);
}
