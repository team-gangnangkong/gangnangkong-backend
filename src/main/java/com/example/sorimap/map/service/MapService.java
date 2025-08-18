// MapService.java
package com.example.sorimap.map.service;

import com.example.sorimap.feed.domain.Feed;
import com.example.sorimap.feed.domain.Sentiment;
import com.example.sorimap.feed.repository.FeedRepository;
import com.example.sorimap.map.dto.ClusterDto;
import com.example.sorimap.map.dto.PanelItemDto;
import com.example.sorimap.search.domain.LocationEntity;
import com.example.sorimap.search.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {

    private final FeedRepository feedRepository;
    private final LocationRepository locationRepository;

    /**
     * 지도 클러스터
     * - locationId가 있으면 해당 위치 중심으로 좌표 범위 제한
     */
    public List<ClusterDto> getClusters(double minLat, double maxLat, double minLng, double maxLng, Long locationId) {
        // locationId가 있으면 중심 좌표 재설정
        if (locationId != null) {
            LocationEntity loc = locationRepository.findById(locationId)
                    .orElseThrow(() -> new IllegalArgumentException("위치를 찾을 수 없습니다."));
            // 여기서는 단순히 locationId 기반으로 전체 범위를 설정하거나, 반경 1km 범위 계산 가능
            double radius = 0.01; // 대략 1km
            minLat = loc.getLatitude() - radius;
            maxLat = loc.getLatitude() + radius;
            minLng = loc.getLongitude() - radius;
            maxLng = loc.getLongitude() + radius;
        }

        List<Feed> feeds = feedRepository.findByLatBetweenAndLngBetween(minLat, maxLat, minLng, maxLng);

        Map<String, List<Feed>> grouped = feeds.stream()
                .collect(Collectors.groupingBy(f -> f.getLat() + "," + f.getLng()));

        List<ClusterDto> clusters = new ArrayList<>();
        for (Map.Entry<String, List<Feed>> entry : grouped.entrySet()) {
            String[] coords = entry.getKey().split(",");
            double lat = Double.parseDouble(coords[0]);
            double lng = Double.parseDouble(coords[1]);

            Map<Sentiment, Long> sentimentCount = entry.getValue().stream()
                    .collect(Collectors.groupingBy(Feed::getSentiment, Collectors.counting()));

            Sentiment dominant = sentimentCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(Sentiment.NEUTRAL);

            clusters.add(new ClusterDto(lat, lng, entry.getValue().size(), dominant));
        }
        return clusters;
    }

    /**
     * 📌 패널 아이템 (줌인 시 상세 목록)
     * - locationId가 있으면 해당 위치 중심으로 좌표 범위 제한
     */
    public List<PanelItemDto> getPanelItems(double minLat, double maxLat, double minLng, double maxLng, Sentiment sentiment, Long locationId) {
        if (locationId != null) {
            LocationEntity loc = locationRepository.findById(locationId)
                    .orElseThrow(() -> new IllegalArgumentException("위치를 찾을 수 없습니다."));
            double radius = 0.01; // 대략 1km
            minLat = loc.getLatitude() - radius;
            maxLat = loc.getLatitude() + radius;
            minLng = loc.getLongitude() - radius;
            maxLng = loc.getLongitude() + radius;
        }

        List<Feed> feeds = feedRepository.findByLatBetweenAndLngBetweenAndSentiment(
                minLat, maxLat, minLng, maxLng, sentiment
        );

        return feeds.stream()
                .map(feed -> new PanelItemDto(
                        feed.getId(),
                        feed.getTitle(),
                        feed.getAddress(),
                        feed.getLikes(),
                        feed.getType(),
                        feed.getLat(),
                        feed.getLng(),
                        feed.getSentiment()
                ))
                .collect(Collectors.toList());
    }
}
