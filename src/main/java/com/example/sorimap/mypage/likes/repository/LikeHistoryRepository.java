package com.example.sorimap.mypage.likes.repository;

import com.example.sorimap.mypage.likes.entity.LikeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeHistoryRepository extends JpaRepository<LikeHistory, Long> {

    @Query(value = "SELECT YEAR(liked_at) as year, WEEK(liked_at, 1) as week, MIN(liked_at) as startDate, COUNT(*) as likeCount " +
            "FROM like_history WHERE user_id = :userId GROUP BY year, week ORDER BY year, week",
            nativeQuery = true)
    List<Object[]> findWeeklyLikeCountsByUserId(@Param("userId") Long userId);

}
