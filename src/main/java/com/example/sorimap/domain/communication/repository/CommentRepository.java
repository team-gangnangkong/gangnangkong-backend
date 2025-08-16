package com.example.sorimap.domain.communication.repository;

import com.example.sorimap.domain.communication.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 댓글(Comment) 엔티티 Repository
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByIssue_IssueIdOrderByCreatedAtAsc(Long issueId);
}
