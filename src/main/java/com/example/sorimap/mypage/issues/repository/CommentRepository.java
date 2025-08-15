package com.example.sorimap.mypage.issues.repository;

import com.example.sorimap.mypage.issues.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}