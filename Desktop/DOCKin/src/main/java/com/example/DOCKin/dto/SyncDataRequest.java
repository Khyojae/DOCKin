package com.example.DOCKin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class SyncDataRequest {
    private String entityName;          // 예: "WorkLogs", "Equipment"
    private String entityId;            // 기본 키 값
    private LocalDateTime clientTimestamp; // 클라이언트에서 변경된 시간 (충돌 해결의 기준)
    private Map<String, Object> changes; // 클라이언트가 변경한 필드 데이터
}