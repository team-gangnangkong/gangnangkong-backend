package com.example.sorimap.mypage.service;

import com.example.sorimap.feed.dto.MyFeedResponseDto;
import com.example.sorimap.feed.service.FeedReadService;
import com.example.sorimap.mypage.dto.LikeHistoryDto;
import com.example.sorimap.mypage.dto.MyPageResponse;
import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.repository.KakaoUserRepository;
import com.example.sorimap.reaction.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final KakaoUserRepository kakaoUserRepository;
    private final ReactionService reactionService;
    private final FeedReadService feedReadService;

    /** 마이페이지 응답 */
    public MyPageResponse getMyPage(Long kakaoId) {
        KakaoUser user = kakaoUserRepository.findById(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. kakaoId=" + kakaoId));

        // 1. 프로필
        String nickname = user.getNickname();
        String profileImageUrl = user.getProfileImageUrl();

        // 2. 공감 히스토리 (카카오 아이디 기준)
        List<LikeHistoryDto> likeHistory = reactionService.getWeeklyLikeHistory(kakaoId);

        // 3. 내가 작성한 민원 (카카오 아이디 기준)
        List<MyFeedResponseDto> myFeeds = feedReadService.getMyFeeds(kakaoId);

        return new MyPageResponse(nickname, profileImageUrl, likeHistory, myFeeds);
    }
}
