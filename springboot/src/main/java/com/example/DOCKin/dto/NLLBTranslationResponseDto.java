package com.example.DOCKin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLLBTranslationResponseDto {

    // API가 반환하는 번역 결과 목록 (번역 결과가 여러 개일 수 있는 경우)
    private List<TranslationResult> translations;

    // 내부 클래스: 실제 번역 내용을 담는 객체
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TranslationResult {

        // 번역된 최종 텍스트
        private String translatedText;

        // (선택 사항) API가 추정하는 원문 언어
        private String detectedSourceLanguage;
    }

    // Service 레이어에서 최종 번역 텍스트를 쉽게 가져오기 위한 Getter (편의 메서드)
    public String getTranslatedText() {
        if (translations != null && !translations.isEmpty()) {
            return translations.get(0).getTranslatedText();
        }
        return null;
    }
}