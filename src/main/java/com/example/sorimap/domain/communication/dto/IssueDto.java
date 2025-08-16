package com.example.sorimap.domain.communication.dto;

import com.example.sorimap.domain.communication.entity.Issue;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글(Issue) 데이터 전송 DTO
 * - Entity → DTO 변환용 fromEntity 제공
 */
@Getter
@Setter
public class IssueDto {
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

    public static IssueDto fromEntity(Issue issue) {
        IssueDto dto = new IssueDto();
        dto.setIssueId(issue.getIssueId());
        dto.setTitle(issue.getTitle());
        dto.setCategory(issue.getCategory());
        dto.setLocation(issue.getLocation());
        dto.setImage(issue.getImage());
        dto.setContent(issue.getContent());
        dto.setLikes(issue.getLikes());
        dto.setSentiment(issue.getSentiment());
        dto.setMarkerColor(issue.getMarkerColor());
        dto.setStatus(issue.getStatus());
        return dto;
    }
}
