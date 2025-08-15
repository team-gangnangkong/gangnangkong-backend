package com.example.sorimap.mypage.issues.repository;

import com.example.sorimap.mypage.issues.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByUserId(Long userId);
}
