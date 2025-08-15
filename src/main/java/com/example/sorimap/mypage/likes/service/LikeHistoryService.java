package com.example.sorimap.mypage.likes.service;

import com.example.sorimap.mypage.likes.dto.LikeHistoryResponse;
import com.example.sorimap.mypage.likes.repository.LikeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class LikeHistoryService {
    private final LikeHistoryRepository likeHistoryRepository;

    public List<LikeHistoryResponse> getWeeklyLikeHistory(Long userId) {
        List<Object[]> results = likeHistoryRepository.findWeeklyLikeCountsByUserId(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");

        return results.stream().map(record -> {
            var startDate = ((java.sql.Date) record[2]).toLocalDate();
            var likeCount = ((Number) record[3]).intValue();

            return new LikeHistoryResponse(startDate.format(formatter), likeCount);
        }).collect(Collectors.toList());
    }
}
