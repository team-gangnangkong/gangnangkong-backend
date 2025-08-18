package com.example.sorimap.reaction.service;

import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.feed.repository.FeedRepository;
import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.repository.KakaoUserRepository;
import com.example.sorimap.mypage.dto.LikeHistoryDto;
import com.example.sorimap.reaction.domain.Reaction;
import com.example.sorimap.reaction.domain.ReactionType;
import com.example.sorimap.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final FeedRepository feedRepository;
    private final KakaoUserRepository kakaoUserRepository;

    /** ✅ 피드 공감하기 */
    public String likeFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다."));
        KakaoUser user = kakaoUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (reactionRepository.findByFeedAndUser(feed, user).isPresent()) {
            return "이미 공감하셨습니다.";
        }

        Reaction reaction = Reaction.builder()
                .feed(feed)
                .user(user)
                .type(ReactionType.LIKE)
                .build();
        reactionRepository.save(reaction);

        feed.setLikes(feed.getLikes() + 1);
        feedRepository.save(feed);

        return "공감 완료";
    }

    /** ✅ 최근 한 달 주차별 공감 히스토리 */
    public List<LikeHistoryDto> getWeeklyLikeHistory(Long userId) {
        KakaoUser user = kakaoUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<Reaction> reactions = reactionRepository.findByUser(user);

        Map<Integer, Long> weekLikeCounts = new HashMap<>();
        WeekFields weekFields = WeekFields.ISO;

        for (Reaction reaction : reactions) {
            if (reaction.getType() == ReactionType.LIKE) {
                int week = reaction.getCreatedAt().get(weekFields.weekOfMonth());
                weekLikeCounts.put(week, weekLikeCounts.getOrDefault(week, 0L) + 1);
            }
        }

        List<LikeHistoryDto> result = new ArrayList<>();
        int month = LocalDateTime.now().getMonthValue();

        // 1~4주 고정 반환
        for (int i = 1; i <= 4; i++) {
            result.add(new LikeHistoryDto(
                    month + "월 " + i + "주",
                    weekLikeCounts.getOrDefault(i, 0L).intValue()
            ));
        }

        return result;
    }
}
