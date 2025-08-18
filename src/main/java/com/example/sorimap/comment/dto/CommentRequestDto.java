package com.example.sorimap.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequestDto {
    private Long feedId;
    private Long userId;
    private String body;
}
