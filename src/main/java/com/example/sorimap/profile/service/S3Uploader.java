package com.example.sorimap.profile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;
    private final String bucketName = "sorimap"; // 👉 application.properties 로 빼는 게 가장 좋음

    /** (기존) 랜덤 파일명으로 업로드 */
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        return upload(file, fileName);
    }

    /** (신규) UserProfileService에서 쓰는 업로드 */
    public String upload(MultipartFile file, String key) {
        try {
            // ✅ 원본 파일명에서 확장자 추출
            String originalFilename = file.getOriginalFilename();
            String ext = ".png"; // 기본 확장자
            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // ✅ 임시 파일 생성 (suffix = 확장자만)
            Path tempFile = Files.createTempFile("upload-", ext);
            file.transferTo(tempFile.toFile());

            // ✅ 업로드 요청 (ACL 제거 → 버킷 정책으로 관리)
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, tempFile);

            log.info("✅ 파일 업로드 완료: {}", key);

            // ✅ S3 원본 URL 반환
            return "https://" + bucketName + ".s3.amazonaws.com/" + key;

        } catch (Exception e) {
            log.error("❌ 파일 업로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

    /** (신규) UserProfileService에서 쓰는 삭제 */
    public void deleteByUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;

        try {
            // URL → key 추출
            String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            log.info("🗑️ 파일 삭제 완료: {}", key);
        } catch (S3Exception e) {
            log.error("❌ 파일 삭제 실패: {}", e.getMessage(), e);
        }
    }
}
