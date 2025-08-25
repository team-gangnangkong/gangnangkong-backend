package com.example.sorimap.profile.service;

import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.repository.KakaoUserRepository;
import com.example.sorimap.profile.dto.ProfileImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final KakaoUserRepository kakaoUserRepository;
    private final S3Uploader s3Uploader;

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
    public ProfileImageResponse updateProfileImage(Long kakaoId, MultipartFile file) {
        KakaoUser user = kakaoUserRepository.findById(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 기존 이미지 삭제 (단, 기본이미지는 삭제하지 않음)
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals("DEFAULT")) {
            s3Uploader.deleteByUrl(user.getProfileImageUrl());
        }

        String newUrl = null;

        if (file != null && !file.isEmpty()) {
            // ✅ 새 파일 업로드 → S3 URL 반환
            validateImageExtension(file.getOriginalFilename());
            String key = "profile/%s-%s".formatted(UUID.randomUUID(), file.getOriginalFilename());
            newUrl = s3Uploader.upload(file, key);
            user.setProfileImageUrl(newUrl);
        } else {
            // ✅ 파일 안 올렸으면 무조건 "DEFAULT" 저장
            user.setProfileImageUrl("DEFAULT");
        }

        kakaoUserRepository.save(user);

        // ✅ 응답: 업로드 했으면 S3 URL, 기본이면 null
        return new ProfileImageResponse(
                "프로필 이미지가 변경되었습니다.",
                (newUrl != null ? newUrl : null)
        );
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
