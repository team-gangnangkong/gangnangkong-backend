package com.example.sorimap.mypage.profile.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.sorimap.mypage.profile.entity.User;
import com.example.sorimap.mypage.profile.repository.UserRepository;
import com.example.sorimap.mypage.profile.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${app.default-profile-image}")
    private String defaultProfileImageUrl;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            // CustomUserDetails에서 User 객체 가져오기
            return userDetails.getUser();
        } else if (principal instanceof String nickname) {
            // 예외적 상황: principal이 문자열일 경우
            return userRepository.findByNickname(nickname)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            throw new RuntimeException("Authentication principal is invalid");
        }
    }


    @Transactional
    public String updateNickname(String nickname) {
        User user = getCurrentUser();

        if (userRepository.existsByNickname(nickname) && !user.getNickname().equals(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        user.setNickname(nickname);
        userRepository.save(user);

        return "프로필 변경 완료되었습니다";
    }


    public String updateProfileImage(MultipartFile file) {
        User user = getCurrentUser();

        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals(defaultProfileImageUrl)) {
            String imageUrl = user.getProfileImageUrl();
            String baseUrl = amazonS3.getUrl(bucket, "").toString(); // 버킷 기본 URL
            String key = imageUrl.replace(baseUrl, "");
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
        }

        String fileUrl;

        try {
            if (file != null && !file.isEmpty()) {
                validateImageExtension(file.getOriginalFilename());

                String fileName = "profile/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));

                fileUrl = amazonS3.getUrl(bucket, fileName).toString();
            } else {
                fileUrl = defaultProfileImageUrl;
            }
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 업로드에 실패했습니다.", e);
        }

        user.setProfileImageUrl(fileUrl);
        userRepository.save(user);

        return fileUrl;
    }

    public String resetProfileImage() {
        User user = getCurrentUser();
        user.setProfileImageUrl(defaultProfileImageUrl);
        userRepository.save(user);
        return defaultProfileImageUrl;
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
