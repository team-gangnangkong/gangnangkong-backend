package com.example.sorimap.profile.controller;

import com.example.sorimap.profile.dto.ProfileImageResponse;
import com.example.sorimap.profile.service.UserProfileService;
import com.example.sorimap.kakao.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final JwtService jwtService;

    /** 닉네임 변경 */
    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@CookieValue("ACCESS-TOKEN") String token,
                                                 @RequestBody Map<String, String> request) {
        Long userId = jwtService.getUserIdFromToken(token);
        String nickname = request.get("nickname");
        return ResponseEntity.ok(userProfileService.updateNickname(userId, nickname));
    }

    /** 프로필 이미지 변경 */
    @PatchMapping("/profile-image")
    public ResponseEntity<ProfileImageResponse> updateProfileImage(
            @CookieValue("ACCESS-TOKEN") String token,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        Long userId = jwtService.getUserIdFromToken(token);
        // ✅ 서비스가 ProfileImageResponse 바로 반환
        ProfileImageResponse response = userProfileService.updateProfileImage(userId, imageFile);

        return ResponseEntity.ok(response);
    }
}
