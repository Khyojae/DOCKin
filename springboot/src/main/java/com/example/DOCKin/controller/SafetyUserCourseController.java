package com.example.DOCKin.controller;

import com.example.DOCKin.model.SafetyCourse;
import com.example.DOCKin.model.SafetyEnrollment;
import com.example.DOCKin.service.SafetyCourseService;
import com.example.DOCKin.service.SafetyEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/safety/courses") // <--- 근로자 전용 경로 (admin 없음)
@RequiredArgsConstructor
public class SafetyUserCourseController {

    private final SafetyCourseService courseService;
    private final SafetyEnrollmentService enrollmentService;
    // 1. 전체 교육 목록 조회 (READ - 근로자용)
    // GET /api/safety/courses
    @GetMapping
    public ResponseEntity<List<SafetyCourse>> getAllCoursesForUser() {
        List<SafetyCourse> courses = courseService.findAllCourses();
        return ResponseEntity.ok(courses);
    }

    // 2. 특정 교육 상세 조회 (READ - 근로자용)
    // GET /api/safety/courses/{courseId}
    @GetMapping("/{courseId}")
    public ResponseEntity<SafetyCourse> getCourseDetailForUser(@PathVariable Integer courseId) {
        SafetyCourse course = courseService.findCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    // 참고: 교육 이수 POST API는 SafetyEnrollmentController에 정의하는 것이 좋습니다.
    @PostMapping("/api/safety/enroll/{courseId}")
    public ResponseEntity<SafetyEnrollment> enrollCourse(@PathVariable Integer courseId) {
        String currentUserId = "U1001"; // 임시 사용자 ID 사용

        // ⭐ 4. service.completeCourse 호출 (Service가 정의되어야 함)
        SafetyEnrollment enrollment = enrollmentService.completeCourse(currentUserId, courseId);

        return ResponseEntity.ok(enrollment);
    }
}