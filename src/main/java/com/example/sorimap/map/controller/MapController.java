// MapController.java
package com.example.sorimap.map.controller;

import com.example.sorimap.map.dto.ClusterDto;
import com.example.sorimap.map.dto.PanelItemDto;
import com.example.sorimap.map.service.MapService;
import com.example.sorimap.feed.domain.Sentiment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    /**
     * 지도 클러스터 가져오기 (감정 필터링 포함)
     * - locationId가 있으면 해당 위치 중심 좌표 기준 반경 검색
     */
    @GetMapping("/clusters")
    public List<ClusterDto> getClusters(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng,
            @RequestParam(required = false) Long locationId // 추가: 특정 위치 ID
    ) {
        return mapService.getClusters(minLat, maxLat, minLng, maxLng, locationId);
    }

    /**
     * 특정 좌표 영역의 패널 아이템 가져오기 (줌인 시)
     */
    @GetMapping("/panel")
    public List<PanelItemDto> getPanelItems(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng,
            @RequestParam Sentiment sentiment,
            @RequestParam(required = false) Long locationId // 추가: 특정 위치 ID
    ) {
        return mapService.getPanelItems(minLat, maxLat, minLng, maxLng, sentiment, locationId);
    }
}
