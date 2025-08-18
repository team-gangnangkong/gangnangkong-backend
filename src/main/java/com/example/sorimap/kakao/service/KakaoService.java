package com.example.sorimap.kakao.service;

import com.example.sorimap.kakao.entity.KakaoUser;
import com.example.sorimap.kakao.repository.KakaoUserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
@Slf4j
public class KakaoService {

    @Value("${kakao.api.key}")
    private String restApiKey;

    @Value("${kakao.redirect_uri.local}")
    private String localRedirectUri;

    @Value("${kakao.redirect_uri.postman}")
    private String postmanRedirectUri;

    // 실행 모드: local 또는 postman (환경에 맞게 변경)
    private final String currentMode = "local";

    private final KakaoUserRepository kakaoUserRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoService(KakaoUserRepository kakaoUserRepository) {
        this.kakaoUserRepository = kakaoUserRepository;
    }

    /**
     * 인가 코드로 카카오 Access Token 발급
     */
    public String kakaoGetAccessViaCode(String code) {
        try {
            String redirectUri = currentMode.equals("postman") ? postmanRedirectUri : localRedirectUri;

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("code", code);
            body.add("client_id", restApiKey);
            body.add("redirect_uri", redirectUri);
            body.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<JsonNode> responseNode = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    entity,
                    JsonNode.class
            );

            if (responseNode.getBody() == null || responseNode.getBody().get("access_token") == null) {
                throw new RuntimeException("카카오 Access Token 발급 실패: " + responseNode);
            }

            return responseNode.getBody().get("access_token").asText();
        } catch (Exception e) {
            log.error("카카오 Access Token 발급 중 오류", e);
            throw new RuntimeException("카카오 Access Token 발급 실패", e);
        }
    }

    /**
     * 액세스 토큰으로 카카오 사용자 정보 조회 및 저장
     */
    @Transactional
    public KakaoUser kakaoGetUserInfoViaAccessToken(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> responseNode = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );

            if (responseNode.getBody() == null) {
                throw new RuntimeException("카카오 사용자 정보 조회 실패: 응답이 null");
            }

            JsonNode userInfo = responseNode.getBody();

            Long kakaoId = userInfo.path("id").asLong();
            String nickname = userInfo.path("properties").path("nickname").asText(null);
            String profileImageUrl = userInfo.path("properties").path("profile_image").asText(null);

            LocalDateTime connectedAt = null;
            String connectedAtString = userInfo.path("connected_at").asText(null);
            if (connectedAtString != null) {
                try {
                    connectedAt = OffsetDateTime.parse(connectedAtString).toLocalDateTime();
                } catch (Exception e) {
                    log.warn("connected_at 파싱 실패: {}", connectedAtString);
                }
            }

            KakaoUser kakaoUser = kakaoUserRepository.findById(kakaoId).orElseGet(() -> {
                KakaoUser newUser = new KakaoUser();
                newUser.setKakaoId(kakaoId);
                return newUser;
            });

            kakaoUser.setNickname(nickname);
            kakaoUser.setProfileImageUrl(profileImageUrl);
            kakaoUser.setConnectedAt(connectedAt);

            kakaoUserRepository.save(kakaoUser);

            return kakaoUser;
        } catch (Exception e) {
            log.error("카카오 사용자 정보 조회 실패", e);
            throw new RuntimeException("카카오 사용자 정보 조회 실패", e);
        }
    }
}
