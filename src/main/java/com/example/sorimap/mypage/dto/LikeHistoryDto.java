package com.example.sorimap.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeHistoryDto {
    private String weekLabel; // 예: "8월 1주"
    private int likeCount;    // 공감 수
}
