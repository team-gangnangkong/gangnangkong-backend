package com.example.sorimap.mypage.issues.dto;

import com.example.sorimap.mypage.issues.entity.Issue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class IssueDetailResponse {

    private Long issueId;
    private String title;
    private String location;
    private String imageUrl;
    private String content;
    private String status;
    private String createdAt;
    private int likeCount;
    private int commentCount;
    private List<CommentResponse> comments;

    @Data
    @AllArgsConstructor
    public static class CommentResponse {
        private String username; // 댓글 작성자
        private String content;  // 댓글 내용
        private String createdAt; // 작성일
    }

    public static IssueDetailResponse fromEntity(Issue issue) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

        List<CommentResponse> commentResponses = issue.getComments().stream()
                .map(c -> new CommentResponse(
                        c.getUser().getNickname(),
                        c.getContent(),
                        c.getCreatedAt().format(formatter)
                ))
                .collect(Collectors.toList());

        // imageUrl이 null이면 기본 이미지 URL 사용
        String imageUrl = issue.getImageUrl() != null ? issue.getImageUrl() :
                "https://your-default-image-url.com/default.png";

        return new IssueDetailResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getLocation(),
                imageUrl,
                issue.getContent(),
                issue.getStatus(),
                issue.getCreatedAt().format(formatter),
                issue.getLikeCount(),
                issue.getCommentCount(),
                commentResponses
        );
    }
}