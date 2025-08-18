package com.example.sorimap.config;

import com.example.sorimap.kakao.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component // ✅ 스프링 빈 등록 필수
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // ✅ 피드 조회(GET)는 로그인 없이 허용
        // 단, "/api/feeds/my" 요청은 내 피드 조회이므로 로그인 필요
        if (uri.startsWith("/api/feeds")
                && "GET".equalsIgnoreCase(method)
                && !uri.startsWith("/api/feeds/my")) {
            log.debug("✅ 공개 피드 조회(GET) 요청 → 로그인 없이 통과 (URI = {})", uri);
            return true;
        }

        // ✅ 나머지는 토큰 검증 필요
        String token = getTokenFromCookies(request);
        if (token == null) {
            log.warn("❌ ACCESS-TOKEN 없음 (URI = {})", uri);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            Long userId = jwtService.getUserIdFromToken(token); // JWT에서 userId 추출
            if (userId == null) {
                throw new IllegalArgumentException("userId 추출 실패");
            }

            // 컨트롤러에서 꺼내쓸 수 있게 userId 저장
            request.setAttribute("userId", userId);
            log.info("✅ JWT 인증 성공: userId = {}, URI = {}", userId, uri);
            return true;
        } catch (Exception e) {
            log.error("❌ JWT 인증 실패 (token = {}), error = {}", token, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private String getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("ACCESS-TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
