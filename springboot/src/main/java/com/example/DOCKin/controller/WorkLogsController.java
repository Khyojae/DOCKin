package com.example.DOCKin.controller;

import com.example.DOCKin.dto.WorkLogsCreateRequestDto;
import com.example.DOCKin.dto.WorkLogsUpdateRequestDto;
import com.example.DOCKin.dto.Work_logsDto;
import com.example.DOCKin.model.MemberUserDetails;
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
            @AuthenticationPrincipal MemberUserDetails userDetails, // ⭐ 타입 변경
            @RequestBody WorkLogsCreateRequestDto requestDto){
        String userId = userDetails.getUsername(); // ⭐ ID 추출
        Work_logsDto newLog = workLogsService.createWorkLog(userId,requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLog);
    }

    @PostMapping("/stt")
    // 타입 통일
    public ResponseEntity<Work_logsDto> processSttAndCreateLog(
            @AuthenticationPrincipal MemberUserDetails userDetails, // ⭐ 수정됨
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") WorkLogsCreateRequestDto metadata){

        String userId = userDetails.getUsername(); // ID 추출
        Work_logsDto newLog = workLogsService.processSttAndSave(userId,file,metadata);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLog);
    }


    @GetMapping
    // 타입 통일
    public ResponseEntity<List<Work_logsDto>> getMyWorkLogs(
            @AuthenticationPrincipal MemberUserDetails userDetails){ // ⭐ 수정됨
        String userId = userDetails.getUsername(); // ID 추출
        List<Work_logsDto> logs = workLogsService.getMyWorkLogs(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{logId}")
    // 타입 유지 및 Null 체크는 선택적으로 유지
    public ResponseEntity<Work_logsDto> getWorkLog(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @PathVariable Long logId){

        // 이 Null 체크는 필터가 실패한 경우를 대비합니다.
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = userDetails.getUsername();
        Work_logsDto log = workLogsService.getWorkLog(userId, logId);
        return ResponseEntity.ok(log);
    }

    @PutMapping("/{logId}")
    // 타입 통일
    public ResponseEntity<Work_logsDto> updateWorkLog(
            @AuthenticationPrincipal MemberUserDetails userDetails, // ⭐ 수정됨
            @PathVariable Long logId,
            @RequestBody WorkLogsUpdateRequestDto requestDto){
        String userId = userDetails.getUsername(); // ID 추출
        Work_logsDto updatedLog = workLogsService.updateWorkLog(userId, logId, requestDto);
        return ResponseEntity.ok(updatedLog);
    }


    @DeleteMapping("/{logId}")
    // 타입 통일
    public ResponseEntity<Void> deleteWorkLog(
            @AuthenticationPrincipal MemberUserDetails userDetails, // ⭐ 수정됨
            @PathVariable Long logId){
        String userId = userDetails.getUsername(); // ID 추출
        workLogsService.deleteWorkLog(userId,logId);
        return ResponseEntity.noContent().build();
    }
}
