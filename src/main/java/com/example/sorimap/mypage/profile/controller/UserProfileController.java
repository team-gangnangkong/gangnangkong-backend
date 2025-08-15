package com.example.sorimap.mypage.profile.controller;
import com.example.sorimap.mypage.profile.dto.ProfileImageResponse;
import com.example.sorimap.mypage.profile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // 닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        userProfileService.updateNickname(nickname);
        return ResponseEntity.ok("프로필 닉네임이 정상적으로 변경되었습니다.");
    }


    // 프로필 이미지 변경 (이미지 없으면 기본 이미지 적용)
    @PatchMapping("/profile-image")
    public ResponseEntity<ProfileImageResponse> updateProfileImage(
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        String imageUrl = userProfileService.updateProfileImage(imageFile);
        return ResponseEntity.ok(new ProfileImageResponse("프로필 이미지가 정상적으로 변경되었습니다.", imageUrl));
    }

}
