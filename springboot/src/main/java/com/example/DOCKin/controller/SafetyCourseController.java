package com.example.DOCKin.controller;

import com.example.DOCKin.dto.CourseCreateRequest;
import com.example.DOCKin.model.SafetyCourse;
import com.example.DOCKin.service.SafetyCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 관리자 권한이 필요한 API
@RestController
@RequestMapping("/api/safety/admin/courses") // <--- 관리자 전용 경로
@RequiredArgsConstructor
public class SafetyCourseController {

    private final SafetyCourseService courseService;

    // 1. 교육 자료 등록 (POST /api/safety/admin/courses)
    @PostMapping // 이 메서드가 교육자료 등록을 담당합니다.
    public ResponseEntity<SafetyCourse> createCourse(@RequestBody CourseCreateRequest request) {
        // 실제 환경에서는 인증/인가를 통해 관리자 ID를 가져와야 함
        String adminId = "1001"; // 임시 관리자 ID

        SafetyCourse newCourse = courseService.createCourse(request, adminId);
        return new ResponseEntity<>(newCourse, HttpStatus.CREATED); // 201 Created
    }

    // 2. 전체 교육 자료 조회 (READ - 관리자도 목록을 봐야 하므로 유지)
    @GetMapping
    public ResponseEntity<List<SafetyCourse>> getAllCourses() {
        List<SafetyCourse> courses = courseService.findAllCourses();
        return ResponseEntity.ok(courses);
    }

    // 3. 특정 교육 상세 조회 (READ - 관리자 전용)
    @GetMapping("/{courseId}")
    public ResponseEntity<SafetyCourse> getCourseDetail(@PathVariable Integer courseId) {
        SafetyCourse course = courseService.findCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    // 4. 교육 자료 수정 (UPDATE)
    @PutMapping("/{courseId}")
    public ResponseEntity<SafetyCourse> updateCourse(@PathVariable Integer courseId,
                                                     @RequestBody CourseCreateRequest request) {
        SafetyCourse updatedCourse = courseService.updateCourse(courseId, request);
        return ResponseEntity.ok(updatedCourse);
    }

    // 5. 교육 자료 삭제 (DELETE)
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // !!! 경고: 여기에 getCourseDetailForUser 또는 getAllCoursesForUser 메서드가 없어야 합니다. !!!
}