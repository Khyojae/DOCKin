package com.example.DOCKin.controller;

import com.example.DOCKin.dto.WorkLogsCreateRequestDto;
import com.example.DOCKin.dto.WorkLogsUpdateRequestDto;
import com.example.DOCKin.dto.Work_logsDto;
import com.example.DOCKin.repository.EquipmentRepository;
import com.example.DOCKin.service.Work_logsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work-logs")
public class WorkLogsController {
    private final Work_logsService workLogsService;


    @PostMapping
    public ResponseEntity<Work_logsDto> createWorkLog(
            @AuthenticationPrincipal String userId,
            @RequestBody WorkLogsCreateRequestDto requestDto){
        Work_logsDto newLog = workLogsService.createWorkLog(userId,requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLog);
    }

    @PostMapping("/stt")
    public ResponseEntity<Work_logsDto> processSttAndCreateLog(
            @AuthenticationPrincipal String userId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") WorkLogsCreateRequestDto metadata){

        Work_logsDto newLog = workLogsService.processSttAndSave(userId,file,metadata);
    return ResponseEntity.status(HttpStatus.CREATED).body(newLog);
    }


    @GetMapping
    public ResponseEntity<List<Work_logsDto>> getMyWorkLogs(
            @AuthenticationPrincipal String userId){
        List<Work_logsDto> logs = workLogsService.getMyWorkLogs(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{logId}")
    public ResponseEntity<Work_logsDto> getWorkLog(
            @AuthenticationPrincipal String userId,
            @PathVariable Long logId){
        Work_logsDto log = workLogsService.getWorkLog(userId, logId);
        return ResponseEntity.ok(log);
    }

    @PutMapping("/{logId}")
    public ResponseEntity<Work_logsDto> updateWorkLog(
            @AuthenticationPrincipal String userId,
            @PathVariable Long logId,
            @RequestBody WorkLogsUpdateRequestDto requestDto){
        Work_logsDto updatedLog = workLogsService.updateWorkLog(userId, logId, requestDto);
    return ResponseEntity.ok(updatedLog);
    }


@DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteWorkLog(
            @AuthenticationPrincipal String userId,
            @PathVariable Long logId){
        workLogsService.deleteWorkLog(userId,logId);
        return ResponseEntity.noContent().build();
}
}
