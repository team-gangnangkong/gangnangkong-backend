package com.example.sorimap.domain.communication.entity;

import com.example.sorimap.domain.communication.dto.CommentDto;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 댓글(Comment) 엔티티
 * - 특정 Issue와 다대일 관계
 */
@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;
    private String author;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    public static Comment toEntity(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setAuthor(dto.getAuthor()); // DTO에서 전달받은 작성자 사용
        return comment;
    }
}
