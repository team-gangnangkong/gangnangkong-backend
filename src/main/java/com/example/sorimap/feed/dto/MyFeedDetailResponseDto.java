package com.example.sorimap.feed.dto;

import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.feed.domain.FeedStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
public class MyFeedDetailResponseDto {
    private Long feedId;
    private String title;
    private String location;
    private String imageUrl;
    private String content;
    private FeedStatus status;
    private String createdAt;
    private int likeCount;
    private int commentCount;
    private String authorNickname;   // ✅ 글 작성자 닉네임
    private List<CommentResponse> comments;

    @Data
    @AllArgsConstructor
    public static class CommentResponse {
        private String username;  // 댓글 작성자 닉네임
        private String content;
        private String createdAt;
    }

    // ✅ 닉네임은 서비스에서 조회해서 파라미터로 전달
    public static MyFeedDetailResponseDto fromEntity(Feed feed,
                                                     int commentCount,
                                                     List<CommentResponse> comments,
                                                     String authorNickname) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

        String imageUrl = feed.getImageUrls() != null
                ? feed.getImageUrls().split(",")[0]
                : "https://your-default-image-url.com/default.png";

        return new MyFeedDetailResponseDto(
                feed.getId(),
                feed.getTitle(),
                feed.getAddress(),
                imageUrl,
                feed.getContent(),
                feed.getStatus(),
                feed.getCreatedAt().format(formatter),
                feed.getLikes(),
                commentCount,
                authorNickname,   // ✅ 서비스에서 가져온 닉네임
                comments
        );
    }
}
