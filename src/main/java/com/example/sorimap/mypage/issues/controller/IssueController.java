package com.example.sorimap.mypage.issues.controller;

import com.example.sorimap.mypage.issues.dto.IssueResponse;
import com.example.sorimap.mypage.issues.dto.IssueDetailResponse;
import com.example.sorimap.mypage.issues.service.IssueService;
import com.example.sorimap.mypage.profile.entity.User;
import com.example.sorimap.mypage.profile.repository.UserRepository;
import com.example.sorimap.mypage.profile.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final UserRepository userRepository;

    // 현재 로그인한 사용자 조회
    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = auth.getPrincipal();

        // UserDetails 기반 처리
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser(); // CustomUserDetails 안에 User 엔티티를 포함했다고 가정
        }
        // String 닉네임 기반 처리
        else if (principal instanceof String nickname) {
            return userRepository.findByNickname(nickname)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            throw new RuntimeException("Authentication principal is invalid");
        }
    }


    // 내 민원 리스트 조회 (검색 기능 제거)
    @GetMapping("/my")
    public List<IssueResponse> getMyIssues() {
        Long userId = getCurrentUser().getId();
        return issueService.getMyIssues(userId);
    }

    // 민원 상세 조회 (토큰 기반)
    @GetMapping("/{issueId}")
    public IssueDetailResponse getIssueDetail(@PathVariable Long issueId) {
        Long userId = getCurrentUser().getId();
        return issueService.getIssueDetail(userId, issueId);
    }
}
