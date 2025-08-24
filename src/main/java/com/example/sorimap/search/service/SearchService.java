package com.example.sorimap.search.service;

import com.example.sorimap.search.domain.LocationEntity;
import com.example.sorimap.search.dto.LocationSelectDto;
import com.example.sorimap.search.dto.SearchResultDto;
import com.example.sorimap.search.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final LocationRepository locationRepository;

    /** (선택) DB 기반 검색을 유지하는 경우만 사용 */
    public List<SearchResultDto> getSearchResults(String keyword) {
        boolean isChosung = keyword.matches("^[ㄱ-ㅎ]+$");
        List<LocationEntity> results = isChosung
                ? locationRepository.findByInitialsContaining(keyword)
                : locationRepository.findByNameContaining(keyword);

        return results.stream()
                .map(loc -> SearchResultDto.builder()
                        .kakaoPlaceId(loc.getKakaoPlaceId())  // ✅ 변경
                        .name(loc.getName())
                        .address(loc.getAddress())
                        .latitude(loc.getLatitude())
                        .longitude(loc.getLongitude())
                        .build())
                .collect(toList());
    }

    /**
     * 장소 선택 저장: kakaoPlaceId 기준 Upsert
     * - 없으면 생성, 있으면 최신 정보 업데이트
     * - 내부 PK(id) 반환(선택)
     */
    @Transactional
    public Long selectLocation(LocationSelectDto dto) {
        if (dto.getKakaoPlaceId() == null || dto.getLatitude() == null || dto.getLongitude() == null) {
            throw new IllegalArgumentException("kakaoPlaceId, latitude, longitude는 필수입니다.");
        }

        LocationEntity entity = locationRepository.findByKakaoPlaceId(dto.getKakaoPlaceId())
                .orElseGet(LocationEntity::new);

        entity.setKakaoPlaceId(dto.getKakaoPlaceId());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        // initials는 필요 시 별도 로직으로 세팅
        return locationRepository.save(entity).getId();
    }
}
