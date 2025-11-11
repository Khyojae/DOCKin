package com.example.DOCKin.service;

import com.example.DOCKin.dto.SyncDataRequest;
import com.example.DOCKin.repository.DynamicRepositoryAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConflictResolutionService {

    // ⭐ DYNAMICALLY ACCESS REPOSITORY LOGIC HERE (실제 구현 시 리플렉션 또는 Repository 패턴 필요)
    // 현재는 로직 설명을 위해 더미(dummy) 메서드를 사용합니다.
    private final DynamicRepositoryAccess dynamicRepositoryAccess;

    /**
     * 오프라인 동기화 충돌을 해결하고 최종 데이터를 DB에 저장합니다.
     * @param request 클라이언트로부터 받은 변경 데이터
     * @return 최종 저장된 엔티티의 최신 서버 타임스탬프
     */
    @Transactional
    public LocalDateTime resolveConflictAndSave(SyncDataRequest request) {

        String entityName = request.getEntityName();
        String entityId = request.getEntityId();
        LocalDateTime clientTimestamp = request.getClientTimestamp();

        // 1. 서버의 현재 상태 및 타임스탬프 조회 (실제로는 DB에서 조회)
        Map<String, Object> serverData = dynamicRepositoryAccess.findDataById(entityName, entityId);
        LocalDateTime serverTimestamp = (LocalDateTime) serverData.get("updatedAt");

        LocalDateTime finalTimestamp;

        if (serverTimestamp == null || clientTimestamp.isAfter(serverTimestamp)) {
            // 2-A. 클라이언트 타임스탬프가 더 최신이거나, 서버에 레코드가 없으면 클라이언트 데이터를 최종본으로 승인 (Last-Write-Wins)
            log.info("Client data is newer. Adopting client changes for {}:{}", entityName, entityId);

            // 3. DB에 최종 데이터 저장 (실제로는 Repository.save() 호출)
            dynamicRepositoryAccess.saveFinalData(entityName, entityId, request.getChanges());
            finalTimestamp = clientTimestamp;

        } else {
            // 2-B. 서버 데이터가 더 최신이거나 같으면 클라이언트 요청을 거부하고 서버 상태 유지
            log.warn("Conflict detected. Server data is newer (Server: {} vs Client: {}). Rejecting client changes.", serverTimestamp, clientTimestamp);
            finalTimestamp = serverTimestamp;

            // 클라이언트에게 최신 서버 데이터 반환 로직 추가 필요 (여기서는 타임스탬프만 반환)
        }

        return finalTimestamp;
    }

    @Transactional(readOnly = true)
    public Map<String, List<Map<String,Object>>> getUpdatesSince(LocalDateTime lastSyncTime){
        log.info("Fetching updates since : {}", lastSyncTime);
        Map<String,List<Map<String,Object>>> updates = new HashMap<>();
        updates.put("WorkLogs", List.of(
                Map.of("logId", 501, "title", "신규 펌프 설치 완료", "updatedAt", LocalDateTime.now().minusMinutes(10))
        ));
        return updates;
    }
}