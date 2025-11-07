package com.example.DOCKin.controller;

import com.example.DOCKin.model.SafetyEnrollment;
import com.example.DOCKin.service.SafetyEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/safety/admin") // <--- 관리자 전용 기본 경로
@RequiredArgsConstructor
public class SafetyAdminController {

    private final SafetyEnrollmentService enrollmentService;

    // 1. 사용자 이수 현황 조회 (GET /api/safety/admin/enrollments)
    // 모든 근로자의 모든 이수 기록을 반환합니다.
    @GetMapping("/enrollments")
    public ResponseEntity<List<SafetyEnrollment>> getAllEnrollments() {
        // Service에 모든 이수 기록을 가져오는 메서드를 호출합니다.
        List<SafetyEnrollment> enrollments = enrollmentService.findAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }
}