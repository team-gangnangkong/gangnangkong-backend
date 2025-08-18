package com.example.sorimap.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    // JWT 토큰 관련 설정값
    private String header;                      // ex) Authorization
    private String secret;                      // JWT 서명용 시크릿 키
    private long tokenValidityInMinutes;        // 액세스 토큰 유효기간 (분)
    private long refreshTokenValidityInMinutes; // 리프레시 토큰 유효기간 (분)
}
