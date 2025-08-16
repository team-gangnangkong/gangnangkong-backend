package com.example.sorimap.domain.communication.controller;

import com.example.sorimap.domain.communication.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 이미지 업로드 요청을 처리하는 컨트롤러
 * - 실제로는 AWS S3 등 외부 저장소에 업로드됨
 */
@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /** 이미지 업로드 */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        String imageUrl = imageService.uploadImage(imageFile);
        return ResponseEntity.ok(imageUrl);
    }
}
