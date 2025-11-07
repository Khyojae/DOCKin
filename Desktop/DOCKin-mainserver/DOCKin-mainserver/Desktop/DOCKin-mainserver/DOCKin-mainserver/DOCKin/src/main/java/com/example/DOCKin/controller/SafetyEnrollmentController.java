package com.example.DOCKin.controller;

import com.example.DOCKin.model.SafetyEnrollment;
import com.example.DOCKin.service.SafetyEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/safety/enroll") // <--- 이수 처리 전용 경로 설정
@RequiredArgsConstructor
public class SafetyEnrollmentController {

    private final SafetyEnrollmentService enrollmentService;

    // POST /api/safety/enroll/{courseId}
    @PostMapping("/{courseId}")
    public ResponseEntity<SafetyEnrollment> enrollCourse(@PathVariable Integer courseId) {
        // 실제 환경에서는 인증/인가를 통해 사용자 ID를 가져와야 합니다.
        String currentUserId = "U1001"; // 임시 사용자 ID 사용

        SafetyEnrollment enrollment = enrollmentService.completeCourse(currentUserId, courseId);

        return ResponseEntity.ok(enrollment);
    }
}