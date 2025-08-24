package com.example.sorimap.search.controller;

import com.example.sorimap.search.dto.LocationSelectDto;
import com.example.sorimap.search.dto.SearchResultDto;
import com.example.sorimap.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    // (DB 기반 검색을 유지한다면)
    @GetMapping
    public ResponseEntity<List<SearchResultDto>> getSearchResults(@RequestParam String keyword) {
        return ResponseEntity.ok(searchService.getSearchResults(keyword));
    }

    /** 장소 선택 기록: kakaoPlaceId 기반 upsert */
    @PostMapping(value = "/select", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> selectLocation(@RequestBody LocationSelectDto dto) {
        Long savedId = searchService.selectLocation(dto);
        return ResponseEntity.ok(savedId); // 내부 PK 반환(프론트에서 안 써도 됨)
    }
}
