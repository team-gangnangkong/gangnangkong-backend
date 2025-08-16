package com.example.sorimap.domain.communication.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * 간단한 규칙 기반 감정 분석 서비스
 * - 키워드 기반으로 긍정/부정/중립 판별
 * - 마커 색상(red/green/yellow) 함께 반환
 */
@Service
public class AiAnalyzeService {
    public Map<String, String> analyzeSentiment(String content) {
        String[] negativeKeywords = {"쓰레기", "악취", "불편", "고장", "파손", "민원", "문제", "개선", "안돼요", "시정"};
        String[] positiveKeywords = {"감사합니다", "좋아요", "아름다운", "만족", "최고", "힐링", "좋은", "행복"};

        int negativeScore = 0;
        int positiveScore = 0;
        String lowerCaseContent = content.toLowerCase();

        for (String keyword : negativeKeywords) {
            if (lowerCaseContent.contains(keyword)) negativeScore++;
        }
        for (String keyword : positiveKeywords) {
            if (lowerCaseContent.contains(keyword)) positiveScore++;
        }

        Map<String, String> result = new HashMap<>();
        if (negativeScore > positiveScore) {
            result.put("sentiment", "부정");
            result.put("markerColor", "red");
        } else if (positiveScore > negativeScore) {
            result.put("sentiment", "긍정");
            result.put("markerColor", "green");
        } else {
            result.put("sentiment", "중립");
            result.put("markerColor", "yellow");
        }
        return result;
    }
}
