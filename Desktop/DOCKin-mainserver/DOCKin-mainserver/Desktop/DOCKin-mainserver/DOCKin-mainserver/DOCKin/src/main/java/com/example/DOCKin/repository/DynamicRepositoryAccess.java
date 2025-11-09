package com.example.DOCKin.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DynamicRepositoryAccess{
    Map<String,Object> findDataById(String entityName, String entityId);
    void saveFinalData(String entityName, String entityId, Map<String, Object>changes);

    List<Map<String, Object>> findUpdatesSince(LocalDateTime lastSyncTime);
}
