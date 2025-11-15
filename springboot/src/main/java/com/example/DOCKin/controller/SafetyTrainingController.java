package com.example.DOCKin.controller;

import com.example.DOCKin.dto.SafetyCourseResponse;
import com.example.DOCKin.model.MemberUserDetails;
import com.example.DOCKin.service.SafetyTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/safety/training")
@RequiredArgsConstructor
public class SafetyTrainingController {

    private final SafetyTrainingService trainingService;

    /**
     * 근로자: 미이수 영상 목록 조회
     * GET : http://localhost:8081/api/safety/training/uncompleted
     */
    @GetMapping("/uncompleted")
    public ResponseEntity<List<SafetyCourseResponse>> getUncompletedVideos(
            @AuthenticationPrincipal MemberUserDetails userDetails) {

        if (userDetails == null) {
            // 인증된 사용자 정보가 없는 경우 (토큰 문제, filter에서 이미 401 처리되지만 안전하게)
            return ResponseEntity.status(401).build();
        }

        String userId = userDetails.getUsername();
        List<SafetyCourseResponse> courses = trainingService.getUncompletedCourses(userId);

        return ResponseEntity.ok(courses);
    }

    @PostMapping("/complete/{videoId}")
    public ResponseEntity<Void> completeCourse(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @PathVariable("videoId") Integer courseId) {

        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        String userId = userDetails.getUsername();
        trainingService.completeCourse(userId, courseId);

        return ResponseEntity.ok().build(); // 200 OK (상태 업데이트 완료)
    }
}