package com.example.sorimap.mypage.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class LikeHistoryResponse {
    private String date;      // ex: "8월 13일"
    private int likeCount;
}
