package com.example.sorimap.domain.communication.repository;

import com.example.sorimap.domain.communication.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 게시글(Issue) 엔티티 Repository
 */
@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByStatus(String status);
    List<Issue> findByCategoryOrderByLikesDesc(String category);
    List<Issue> findAllByOrderByLikesDesc();
}
