package com.example.DOCKin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLLBTranslationRequestDto {

    // 번역할 원문 텍스트
    private String text;

    // 원문 언어 코드 (예: "en", "ko")
    private String source_language;

    // 번역할 대상 언어 코드 (예: "ko", "en")
    private String target_language;

    // (선택 사항) 모델 버전이나 특수 옵션이 필요하다면 추가
    // private String model_version;
}