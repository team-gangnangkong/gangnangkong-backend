package com.example.sorimap.mypage.dto;

import com.example.sorimap.feed.dto.MyFeedResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyPageResponse {
    private String nickname;                   // 닉네임
    private String profileImageUrl;            // 프로필 이미지
    private List<LikeHistoryDto> likeHistory;  // 공감 히스토리
    private List<MyFeedResponseDto> myFeeds;   // 내가 작성한 민원
}
