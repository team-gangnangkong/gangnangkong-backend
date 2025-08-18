package com.example.sorimap.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String body;
    private String nickname;          // ✅ userId → nickname
    private LocalDateTime createdAt;
}
