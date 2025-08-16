package com.example.sorimap.domain.communication.entity;

import com.example.sorimap.domain.communication.dto.IssueDto;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

/**
 * 게시글(Issue) 엔티티
 * - 민원/문화 게시글 저장
 * - 댓글(Comment)와 일대다 관계
 */
@Getter
@Setter
@Entity
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    private String title;
    private String category;
    private String location;
    private String image;
    private String content;
    private int likes;
    private String sentiment;
    private String markerColor;
    private String status;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public static Issue toEntity(IssueDto dto) {
        Issue issue = new Issue();
        issue.setTitle(dto.getTitle());
        issue.setCategory(dto.getCategory());
        issue.setLocation(dto.getLocation());
        issue.setImage(dto.getImage());
        issue.setContent(dto.getContent());
        issue.setLikes(dto.getLikes());
        issue.setSentiment(dto.getSentiment());
        issue.setMarkerColor(dto.getMarkerColor());
        issue.setStatus(dto.getStatus());
        return issue;
    }
}
