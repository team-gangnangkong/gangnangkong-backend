package com.example.sorimap.profile.service;

import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.repository.KakaoUserRepository; // ✅ 패키지 수정
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import com.example.sorimap.profile.service.S3Uploader;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final KakaoUserRepository kakaoUserRepository; // ✅ 이제 정상 인식됨
    private final S3Uploader s3Uploader;

    @Value("${app.default-profile-image}")
    private String defaultProfileImageUrl;

    /** 닉네임 변경 */
    @Transactional
    public String updateNickname(Long kakaoId, String nickname) {
        KakaoUser user = kakaoUserRepository.findById(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (kakaoUserRepository.existsByNickname(nickname) && !user.getNickname().equals(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        user.setNickname(nickname);
        kakaoUserRepository.save(user);

        return "닉네임이 정상적으로 변경되었습니다.";
    }

    /** 프로필 이미지 변경 */
    @Transactional
    public String updateProfileImage(Long kakaoId, MultipartFile file) {
        KakaoUser user = kakaoUserRepository.findById(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals(defaultProfileImageUrl)) {
            s3Uploader.deleteByUrl(user.getProfileImageUrl());
        }

        String newUrl = defaultProfileImageUrl;
        if (file != null && !file.isEmpty()) {
            validateImageExtension(file.getOriginalFilename());
            String key = "profile/%s-%s".formatted(UUID.randomUUID(), file.getOriginalFilename());
            newUrl = s3Uploader.upload(file, key);
        }

        user.setProfileImageUrl(newUrl);
        kakaoUserRepository.save(user);
        return newUrl;
    }

    private void validateImageExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("올바른 파일 이름이 아닙니다.");
        }
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if (!(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png"))) {
            throw new IllegalArgumentException("이미지 확장자는 jpg, jpeg, png만 가능합니다.");
        }
    }
}
