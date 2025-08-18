package com.example.sorimap.comment.repository;

import com.example.sorimap.comment.domain.Comment;
import com.example.sorimap.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // ✅ 특정 피드의 댓글 목록 (최신순)
    List<Comment> findByFeedIdOrderByCreatedAtDesc(Long feedId);

    // ✅ 특정 피드의 댓글 개수
    long countByFeedId(Long feedId);
}
