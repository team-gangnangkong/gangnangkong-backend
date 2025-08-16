package com.example.sorimap.domain.communication.service;

import com.example.sorimap.domain.communication.dto.CommentDto;
import com.example.sorimap.domain.communication.dto.IssueDto;
import com.example.sorimap.domain.communication.entity.Comment;
import com.example.sorimap.domain.communication.entity.Issue;
import com.example.sorimap.domain.communication.entity.IssueStatus;
import com.example.sorimap.global.exception.EntityNotFoundException;
import com.example.sorimap.domain.communication.repository.CommentRepository;
import com.example.sorimap.domain.communication.repository.IssueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

    /**
     * 게시글(Issue) 및 댓글(Comment) 관련 비즈니스 로직 처리
     */
    @Service
    public class PostService {
    private final IssueRepository issueRepository;
    private final CommentRepository commentRepository;
    private final AiAnalyzeService aiAnalyzeService;

    public PostService(IssueRepository issueRepository, CommentRepository commentRepository, AiAnalyzeService aiAnalyzeService) {
        this.issueRepository = issueRepository;
        this.commentRepository = commentRepository;
        this.aiAnalyzeService = aiAnalyzeService;
    }

    /** 게시글 등록 */
    @Transactional
    public IssueDto createIssue(IssueDto issueDto) {
        // AI 감정 분석
        Map<String, String> sentimentResult = aiAnalyzeService.analyzeSentiment(issueDto.getContent());

        // 분석 결과 반영
        issueDto.setSentiment(sentimentResult.get("sentiment"));
        issueDto.setMarkerColor(sentimentResult.get("markerColor"));

        // 민원 카테고리일 경우 기본 상태를 RECEIVED로 설정
        if ("민원".equals(issueDto.getCategory())) {
            issueDto.setStatus(IssueStatus.RECEIVED.name());
        }

        Issue issue = Issue.toEntity(issueDto);
        Issue savedIssue = issueRepository.save(issue);
        return IssueDto.fromEntity(savedIssue);
    }

    /** 피드 조회 */
    @Transactional(readOnly = true)
    public List<IssueDto> getFeeds(String category, String sentiment) {
        List<Issue> issues;
        if ("전체".equals(category)) {
            issues = issueRepository.findAllByOrderByLikesDesc();
        } else {
            issues = issueRepository.findByCategoryOrderByLikesDesc(category);
        }
        if (sentiment != null && !"ALL".equals(sentiment)) {
            issues = issues.stream()
                    .filter(issue -> sentiment.equals(issue.getSentiment()))
                    .collect(Collectors.toList());
        }
        return issues.stream().map(IssueDto::fromEntity).collect(Collectors.toList());
    }

    /** 상세 조회 */
    @Transactional(readOnly = true)
    public IssueDto getIssueDetail(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이슈를 찾을 수 없습니다."));
        return IssueDto.fromEntity(issue);
    }

    /** 진행 중 이슈 조회 */
    @Transactional(readOnly = true)
    public List<IssueDto> getInProgressIssues(String status) {
        if (!IssueStatus.IN_PROGRESS.name().equals(status)) {
            throw new IllegalArgumentException("유효하지 않은 상태값입니다.");
        }
        List<Issue> issues = issueRepository.findByStatus(status);
        return issues.stream().map(IssueDto::fromEntity).collect(Collectors.toList());
    }

    /** 댓글 등록 */
    @Transactional
    public CommentDto addComment(Long issueId, CommentDto commentDto) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이슈를 찾을 수 없습니다."));

        Comment comment = Comment.toEntity(commentDto);
        comment.setIssue(issue);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.fromEntity(savedComment);
    }

    /** 댓글 조회 */
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long issueId) {
        List<Comment> comments = commentRepository.findByIssue_IssueIdOrderByCreatedAtAsc(issueId);
        return comments.stream().map(CommentDto::fromEntity).collect(Collectors.toList());
    }
}
