package com.example.sorimap.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//  전역 웹 설정 클래스
// - CORS 설정
// - 정적 리소스 핸들러 설정
// - 메시지 컨버터 설정 (선택)
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOriginPatterns(
                        "http://localhost:5500",   // 프론트엔드 개발 환경
                        "http://localhost:8080",   // 개발 환경
                        "https://sorimap.com",     // 운영 환경
                        "https://*.sorimap.com"    // 서브 도메인 허용
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 쿠키/인증 정보 전송 허용
    }


    //  정적 리소스 매핑 (파일 업로드/이미지 제공 시 사용)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/") // 실제 서버 경로
                .setCachePeriod(3600); // 캐싱 시간 (1시간)
    }

    //  JSON 메시지 컨버터 추가 (필요할 때만)
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
