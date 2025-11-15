package com.example.DOCKin.controller;

import com.example.DOCKin.dto.SyncDataRequest;
import com.example.DOCKin.service.ConflictResolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
public class SyncController {
    private final ConflictResolutionService conflictResolutionService;

    @PostMapping("/resolve")
    public ResponseEntity<LocalDateTime> resolveSyncConflict(@RequestBody SyncDataRequest request){
        LocalDateTime finalTimeStamp = conflictResolutionService.resolveConflictAndSave(request);
        return ResponseEntity.ok(finalTimeStamp);
    }

    @GetMapping("/updates")
    public ResponseEntity<Map<String, List<Map<String,Object>>>> getSyncUpdates(
            @RequestParam("lastSyncTime") LocalDateTime lastSyncTime){

        Map<String, List<Map<String,Object>>> updates =
                conflictResolutionService.getUpdatesSince(lastSyncTime);
        return ResponseEntity.ok(updates);
    }

}
