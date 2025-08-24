package com.example.sorimap.feed.service;

import com.example.sorimap.comment.repository.CommentRepository;
import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.feed.domain.FeedStatus;
import com.example.sorimap.feed.dto.FeedResponseDto;
import com.example.sorimap.feed.dto.MyFeedDetailResponseDto;
import com.example.sorimap.feed.dto.MyFeedResponseDto;
import com.example.sorimap.feed.repository.FeedRepository;
import com.example.sorimap.kakao.repository.KakaoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedReadService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final KakaoUserRepository kakaoUserRepository;

    /** 전체 커뮤니티 조회 */
    public List<FeedResponseDto> getAllFeeds(String kakaoPlaceId) {
        List<Feed> feeds = (kakaoPlaceId == null)
                ? feedRepository.findAll()
                : feedRepository.findByLocation_KakaoPlaceId(kakaoPlaceId);

        return feeds.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /** 상태별 조회 + 장소 필터 */
    public List<FeedResponseDto> getFeedsByStatus(FeedStatus status, String kakaoPlaceId) {
        List<Feed> feeds = (kakaoPlaceId == null)
                ? feedRepository.findByStatus(status)
                : feedRepository.findByStatusAndLocation_KakaoPlaceId(status, kakaoPlaceId);

        return feeds.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /** 단건 조회 */
    public FeedResponseDto getFeedById(Long id) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드를 찾을 수 없습니다."));
        return convertToDto(feed);
    }

    /** 내가 작성한 민원 목록 */
    public List<MyFeedResponseDto> getMyFeeds(Long userId) {
        return feedRepository.findByUserId(userId).stream()
                .map(feed -> {
                    int commentCount = (int) commentRepository.countByFeedId(feed.getId());
                    String nickname = kakaoUserRepository.findById(feed.getUserId())
                            .map(u -> u.getNickname())
                            .orElse("알 수 없음");
                    return MyFeedResponseDto.fromEntity(feed, commentCount, nickname);
                })
                .collect(Collectors.toList());
    }

    /** 내가 작성한 민원 상세 */
    public MyFeedDetailResponseDto getMyFeedDetail(Long userId, Long feedId) {
        Feed feed = feedRepository.findByIdAndUserId(feedId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 민원이 존재하지 않거나 본인의 글이 아닙니다."));

        int commentCount = (int) commentRepository.countByFeedId(feedId);

        List<MyFeedDetailResponseDto.CommentResponse> comments =
                commentRepository.findByFeedIdOrderByCreatedAtDesc(feedId).stream()
                        .map(c -> {
                            String nickname = kakaoUserRepository.findById(c.getUserId())
                                    .map(u -> u.getNickname())
                                    .orElse("알 수 없음");
                            return new MyFeedDetailResponseDto.CommentResponse(
                                    nickname,
                                    c.getBody(),
                                    c.getCreatedAt().toString()
                            );
                        })
                        .toList();

        String authorNickname = kakaoUserRepository.findById(feed.getUserId())
                .map(u -> u.getNickname())
                .orElse("알 수 없음");

        return MyFeedDetailResponseDto.fromEntity(feed, commentCount, comments, authorNickname);
    }

    /** Feed → FeedResponseDto 변환 */
    private FeedResponseDto convertToDto(Feed feed) {
        List<String> images = null;
        if (feed.getImageUrls() != null && !feed.getImageUrls().isBlank()) {
            images = Arrays.asList(feed.getImageUrls().split(","));
        }

        String nickname = kakaoUserRepository.findById(feed.getUserId())
                .map(u -> u.getNickname())
                .orElse("알 수 없음");

        return FeedResponseDto.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .type(feed.getType())
                .sentiment(feed.getSentiment())
                .status(feed.getStatus())
                .address(feed.getAddress())
                .lat(feed.getLat())
                .lng(feed.getLng())
                .kakaoPlaceId(feed.getLocation() != null ? feed.getLocation().getKakaoPlaceId() : null)
                .likes(feed.getLikes())
                .imageUrls(images)
                .userNickname(nickname)
                .createdAt(feed.getCreatedAt())
                .build();
    }
}


