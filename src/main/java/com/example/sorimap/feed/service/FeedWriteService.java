package com.example.sorimap.feed.service;

import com.example.sorimap.feed.domain.*;
import com.example.sorimap.feed.dto.FeedRequestDto;
import com.example.sorimap.feed.repository.FeedRepository;
import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.repository.KakaoUserRepository;
import com.example.sorimap.search.domain.LocationEntity;
import com.example.sorimap.search.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedWriteService {

    private final FeedRepository feedRepository;
    private final KakaoUserRepository kakaoUserRepository;
    private final LocationRepository locationRepository;
    private final FeedImageService feedImageService; // ✅ 이미지 업로드 서비스

    @Transactional
    public Feed createFeed(FeedRequestDto dto, Long userId, List<MultipartFile> images) throws IOException {
        KakaoUser user = kakaoUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 📍 위치 처리 (kakaoPlaceId 기준)
        if (dto.getKakaoPlaceId() == null) {
            throw new IllegalArgumentException("kakaoPlaceId는 필수입니다.");
        }

        LocationEntity location = locationRepository.findByKakaoPlaceId(dto.getKakaoPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("선택한 장소를 찾을 수 없습니다."));

        // 🖼️ 이미지 업로드 (여러 장)
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String url = feedImageService.uploadImage(image);
                imageUrls.add(url);
            }
        }

        // 카테고리/감정 분석
        FeedType type = classifyFeedType(dto.getTitle(), dto.getContent());
        Sentiment sentiment = analyzeSentiment(dto.getContent());

        Feed feed = Feed.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .type(type)
                .sentiment(sentiment)
                .status(FeedStatus.OPEN)
                .address(dto.getAddress())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .likes(0)
                .imageUrls(String.join(",", imageUrls))
                .location(location)          // ✅ FK로 저장
                .userId(user.getKakaoId())   // 작성자
                .build();

        return feedRepository.save(feed);
    }

    // 카테고리 분류
    private FeedType classifyFeedType(String title, String content) {
        String text = (title + " " + content).toLowerCase();

        String[] cultureKeywords = {
                "식당", "음식점", "카페", "커피", "음식", "맛집", "사장님", "친절",
                "맛있다", "맛있음", "분위기", "인테리어", "카페인", "디저트", "케이크",
                "메뉴", "차분하다", "아늑하다", "위생", "청결", "깨끗", "깔끔",
                "조용하다", "음향", "공간", "서비스", "응대", "가격", "합리적"
        };

        String[] complaintKeywords = {
                "쓰레기", "음식물", "냄새", "악취", "소음", "시끄럽다", "소란",
                "어둡다", "가로등", "불빛", "불법", "주정차", "정체", "교통",
                "위험", "파손", "고장", "불편", "신고", "불친절", "막다", "차단",
                "위생 문제", "더럽다", "불결", "부정", "민원", "위험하다"
        };

        for (String keyword : cultureKeywords) {
            if (text.contains(keyword)) {
                return FeedType.MUNHWA;
            }
        }

        for (String keyword : complaintKeywords) {
            if (text.contains(keyword)) {
                return FeedType.MINWON;
            }
        }

        return FeedType.MUNHWA;
    }

    // 감정 분석
    private Sentiment analyzeSentiment(String content) {
        String lower = content.toLowerCase();

        String[] positiveKeywords = {
                "좋다", "좋아요", "만족", "추천", "친절", "깔끔", "맛있다", "맛있음",
                "훌륭", "최고", "감사", "만점", "아늑하다", "편안", "쾌적",
                "깨끗", "청결", "합리적", "저렴", "기분좋다", "감동"
        };

        String[] negativeKeywords = {
                "나쁘다", "별로", "불편", "불친절", "최악", "비싸다", "더럽다", "불결",
                "위험", "시끄럽다", "냄새", "악취", "실망", "짜증", "불만",
                "위생 문제", "느리다", "답답", "불법", "파손", "고장"
        };

        for (String keyword : positiveKeywords) {
            if (lower.contains(keyword)) {
                return Sentiment.POSITIVE;
            }
        }

        for (String keyword : negativeKeywords) {
            if (lower.contains(keyword)) {
                return Sentiment.NEGATIVE;
            }
        }

        return Sentiment.NEUTRAL;
    }
}
