package com.example.sorimap.domain.communication.controller;

import com.example.sorimap.domain.communication.dto.CommentDto;
import com.example.sorimap.domain.communication.dto.IssueDto;
import com.example.sorimap.domain.communication.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 커뮤니티 게시글(Issue) 및 댓글(Comment) 관련 요청을 처리하는 컨트롤러
 * - 게시글 생성, 상세조회, 피드조회
 * - 진행 중인 이슈 조회
 * - 댓글 등록 및 조회
 */
@RestController
@RequestMapping("/issues")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /** 게시글 등록 */
    @PostMapping
    public ResponseEntity<IssueDto> createIssue(@RequestBody IssueDto issueDto) {
        IssueDto createdIssue = postService.createIssue(issueDto);
        return ResponseEntity.ok(createdIssue);
    }

    /** 게시글 피드 조회 (카테고리 + 감정 분석 필터) */
    @GetMapping("/feeds")
    public ResponseEntity<List<IssueDto>> getFeeds(
            @RequestParam(name = "type") String category,
            @RequestParam(name = "sentiment", required = false) String sentiment) {
        List<IssueDto> feeds = postService.getFeeds(category, sentiment);
        return ResponseEntity.ok(feeds);
    }

    /** 게시글 상세 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<IssueDto> getIssueDetail(@PathVariable("id") Long issueId) {
        IssueDto issueDetail = postService.getIssueDetail(issueId);
        return ResponseEntity.ok(issueDetail);
    }

    /** 진행 중인 이슈 조회 */
    @GetMapping(params = "status")
    public ResponseEntity<List<IssueDto>> getInProgressIssues(@RequestParam("status") String status) {
        List<IssueDto> inProgressIssues = postService.getInProgressIssues(status);
        return ResponseEntity.ok(inProgressIssues);
    }

    /** 댓글 등록 */
    @PostMapping("/{issueId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("issueId") Long issueId,
                                                    @RequestBody CommentDto commentDto) {
        CommentDto createdComment = postService.addComment(issueId, commentDto);
        return ResponseEntity.ok(createdComment);
    }

    /** 댓글 조회 */
    @GetMapping("/{issueId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable("issueId") Long issueId) {
        List<CommentDto> comments = postService.getComments(issueId);
        return ResponseEntity.ok(comments);
    }
}
