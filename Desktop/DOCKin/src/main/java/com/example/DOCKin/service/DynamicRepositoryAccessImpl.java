package com.example.DOCKin.service;

import com.example.DOCKin.repository.DynamicRepositoryAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ConflictResolutionService가 요구하는 Bean을 제공합니다.
@Service
@Slf4j // ⭐ 로깅 기능 활성화
public class DynamicRepositoryAccessImpl implements DynamicRepositoryAccess {

    @Override
    public Map<String, Object> findDataById(String entityName, String entityId) {
        if ("WorkLogs".equals(entityName)) {

            // --- 시나리오 1: Client Wins (1001) ---
            // 서버 데이터가 클라이언트보다 오래됨
            if ("1001".equals(entityId)) {
                Map<String, Object> data = new HashMap<>();
                // 서버 데이터는 클라이언트 타임스탬프보다 훨씬 이전(2시간 전)
                data.put("updatedAt", LocalDateTime.now().minusHours(2));
                data.put("currentTitle", "서버의 2시간 전 제목");
                return data;
            }

            // --- 시나리오 2: Server Wins (1002) ---
            // 서버 데이터가 클라이언트보다 최신
            if ("1002".equals(entityId)) {
                Map<String, Object> data = new HashMap<>();
                // 서버 데이터는 클라이언트 타임스탬프보다 이후(10분 전)
                data.put("updatedAt", LocalDateTime.now().minusMinutes(10));
                data.put("currentTitle", "서버가 10분 전에 업데이트한 최신 제목");
                return data;
            }
        }
        // 새 레코드이거나 다른 엔티티는 서버 상태 없음으로 처리
        return new HashMap<>();
    }

    @Override
    public void saveFinalData(String entityName, String entityId, Map<String, Object> changes) {
        log.info("MOCK DB SAVE: Final data saved for {}:{} with changes: {}", entityName, entityId, changes);
    }

    @Override
    public List<Map<String, Object>> findUpdatesSince(LocalDateTime lastSyncTime) {
        // GET /updates 엔드포인트의 더미 데이터 반환
        return List.of(
                Map.of("logId", 501, "title", "신규 펌프 설치 완료 (Server)", "updatedAt", LocalDateTime.now().minusMinutes(5)),
                Map.of("logId", 502, "title", "점검 리포트 생성 (Server)", "updatedAt", LocalDateTime.now().minusMinutes(3))
        );
    }
}