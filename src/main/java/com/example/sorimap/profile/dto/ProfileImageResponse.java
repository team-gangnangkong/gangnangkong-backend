package com.example.sorimap.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileImageResponse {
    private String message;
    private String imageUrl; // 기본이미지일 때는 null
}
