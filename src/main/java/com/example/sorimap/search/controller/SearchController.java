package com.example.sorimap.search.controller;

import com.example.sorimap.search.dto.LocationSelectDto;
import com.example.sorimap.search.dto.SearchResultDto;
import com.example.sorimap.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    /**
     * 키워드 검색 (초성/일반 검색 통합)
     */
    @GetMapping
    public ResponseEntity<List<SearchResultDto>> getSearchResults(@RequestParam String keyword) {
        return ResponseEntity.ok(searchService.getSearchResults(keyword));
    }

    /**
     * 장소 선택 시 서버로 전달 (위치 ID, 좌표 포함)
     */
    @PostMapping("/select")
    public ResponseEntity<Void> selectLocation(@RequestBody LocationSelectDto dto) {
        searchService.selectLocation(dto);
        return ResponseEntity.ok().build();
    }
}
