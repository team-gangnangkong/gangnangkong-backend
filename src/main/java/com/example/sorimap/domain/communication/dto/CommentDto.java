package com.example.sorimap.domain.communication.dto;

import com.example.sorimap.domain.communication.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 댓글(Comment) 데이터 전송 DTO
 */
@Getter
@Setter
public class CommentDto {
    private Long commentId;
    private Long issueId;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public static CommentDto fromEntity(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setCommentId(comment.getCommentId());
        dto.setIssueId(comment.getIssue().getIssueId());
        dto.setContent(comment.getContent());
        dto.setAuthor(comment.getAuthor());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
