package com.example.sorimap.search.service;

import com.example.sorimap.search.domain.LocationEntity;
import com.example.sorimap.search.dto.LocationSelectDto;
import com.example.sorimap.search.dto.SearchResultDto;
import com.example.sorimap.search.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final LocationRepository locationRepository;

    /**
     * 키워드로 검색 결과 반환 (초성/일반 검색 통합)
     */
    public List<SearchResultDto> getSearchResults(String keyword) {
        boolean isChosung = keyword.matches("^[ㄱ-ㅎ]+$"); // 초성만 있는지 판별

        List<LocationEntity> results;
        if (isChosung) {
            // 초성 검색
            results = locationRepository.findByInitialsContaining(keyword);
        } else {
            // 일반 한글/영문 검색
            results = locationRepository.findByNameContaining(keyword);
        }

        return results.stream()
                .map(loc -> SearchResultDto.builder()
                        .id(loc.getId())
                        .name(loc.getName())
                        .address(loc.getAddress())
                        .latitude(loc.getLatitude())
                        .longitude(loc.getLongitude())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 사용자가 위치 선택 시 처리
     * - 여기서는 DB에서 확인만 하지만, 필요시 세션/로그 저장 가능
     */
    public void selectLocation(LocationSelectDto dto) {
        locationRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 위치를 찾을 수 없습니다."));
        // 선택된 위치를 이용해 지도/피드 필터링 로직과 연동 가능
    }
}
