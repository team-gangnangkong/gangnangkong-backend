package com.example.sorimap.mypage.issues.service;

import com.example.sorimap.mypage.issues.dto.IssueResponse;
import com.example.sorimap.mypage.issues.dto.IssueDetailResponse;
import com.example.sorimap.mypage.issues.entity.Issue;
import com.example.sorimap.mypage.issues.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;

    // 내 민원 전체 조회
    public List<IssueResponse> getMyIssues(Long userId) {
        List<Issue> issues = issueRepository.findByUserId(userId);

        return issues.stream()
                .map(i -> new IssueResponse(
                        i.getId(),
                        i.getTitle(),
                        i.getLocation(),
                        i.getLikeCount(),
                        i.getCommentCount(),
                        i.getImageUrl() != null ? i.getImageUrl()
                                : "https://your-default-image-url.com/default.png"
                ))
                .collect(Collectors.toList());
    }

    // 내 민원 상세 조회
    public IssueDetailResponse getIssueDetail(Long userId, Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        if (!issue.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        return IssueDetailResponse.fromEntity(issue);
    }
}
