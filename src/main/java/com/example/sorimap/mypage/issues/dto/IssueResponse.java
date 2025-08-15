package com.example.sorimap.mypage.issues.dto;

import lombok.*;

@Data
@AllArgsConstructor

public class IssueResponse {
    private Long issueId;
    private String title;
    private String location;
    private int likeCount;
    private int commentCount;
    private String imageUrl;
}
