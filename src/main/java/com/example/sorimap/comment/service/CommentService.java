package com.example.sorimap.comment.service;

import com.example.sorimap.comment.domain.Comment;
import com.example.sorimap.comment.dto.CommentRequestDto;
import com.example.sorimap.comment.dto.CommentResponseDto;
import com.example.sorimap.comment.repository.CommentRepository;
import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.feed.repository.FeedRepository;
import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.repository.KakaoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /** 특정 피드 댓글 조회 */
    public List<CommentResponseDto> getComments(Long feedId) {
        return commentRepository.findByFeedIdOrderByCreatedAtDesc(feedId)
                .stream()
                .map(c -> new CommentResponseDto(
                        c.getId(),
                        c.getBody(),
                        c.getUserId().toString(),   // ✅ UserId 기반 (닉네임 필요하면 KakaoUserRepo 통해 조회)
                        c.getCreatedAt()
                ))
                .toList();
    }

    /** 댓글 작성 */
    public Comment createComment(CommentRequestDto dto, Long userId) {
        Comment comment = Comment.builder()
                .feedId(dto.getFeedId())  // ✅ FeedId 기반
                .userId(userId)           // ✅ UserId 기반
                .body(dto.getBody())
                .build();
        return commentRepository.save(comment);
    }
}
