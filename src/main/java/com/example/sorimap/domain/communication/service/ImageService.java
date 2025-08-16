package com.example.sorimap.domain.communication.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.util.UUID;

/**
 * 이미지 업로드 서비스
 * - MultipartFile을 받아 AWS S3에 업로드하고 URL을 반환합니다.
 * - S3Client는 S3Config 클래스를 통해 주입받습니다.
 */
@Service
public class ImageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일은 비어 있을 수 없습니다.");
        }

        // 파일명 충돌을 피하기 위해 고유한 파일명 생성
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        try {
            // 파일을 S3에 업로드하기 위한 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(imageFile.getContentType())
                    .contentLength(imageFile.getSize())
                    .build();

            // 파일 업로드 실행
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(imageFile.getInputStream(), imageFile.getSize()));

            // 업로드된 파일의 공개 URL 반환
            return "https://" + bucketName + "[.s3.amazonaws.com/](https://.s3.amazonaws.com/)" + fileName;

        } catch (IOException e) {
            throw new IOException("S3에 이미지 업로드 실패", e);
        }
    }
}
