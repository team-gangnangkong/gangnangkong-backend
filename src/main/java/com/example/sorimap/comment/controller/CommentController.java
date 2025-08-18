package com.example.sorimap.comment.controller;

import com.example.sorimap.comment.domain.Comment;
import com.example.sorimap.comment.dto.CommentRequestDto;
import com.example.sorimap.comment.dto.CommentResponseDto;
import com.example.sorimap.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{feedId}")
    public List<CommentResponseDto> getComments(@PathVariable Long feedId) {
        return commentService.getComments(feedId);
    }

    @PostMapping
    public Comment createComment(HttpServletRequest request,
                                 @RequestBody CommentRequestDto dto) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        return commentService.createComment(dto, userId);
    }
}
