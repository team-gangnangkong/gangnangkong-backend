package com.example.sorimap.feed.dto;

import com.example.sorimap.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyFeedResponseDto {
    private Long feedId;
    private String title;
    private String location;
    private int likeCount;
    private int commentCount;
    private String imageUrl;
    private String authorNickname;   // ✅ 글 작성자 닉네임

    // ✅ 닉네임은 서비스에서 조회 후 파라미터로 받음
    public static MyFeedResponseDto fromEntity(Feed feed, int commentCount, String authorNickname) {
        String imageUrl = feed.getImageUrls() != null
                ? feed.getImageUrls().split(",")[0]
                : "https://your-default-image-url.com/default.png";

        return new MyFeedResponseDto(
                feed.getId(),
                feed.getTitle(),
                feed.getAddress(),
                feed.getLikes(),
                commentCount,
                imageUrl,
                authorNickname  // ✅ 서비스에서 전달된 닉네임
        );
    }
}
