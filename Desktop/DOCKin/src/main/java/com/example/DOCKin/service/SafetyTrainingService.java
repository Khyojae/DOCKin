package com.example.DOCKin.service;

import com.example.DOCKin.dto.SafetyCourseResponse;
import com.example.DOCKin.model.SafetyCourse;
import com.example.DOCKin.model.SafetyEnrollment;
import com.example.DOCKin.repository.SafetyCourseRepository;
import com.example.DOCKin.repository.SafetyEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SafetyTrainingService {

    private final SafetyEnrollmentRepository enrollmentRepository;
    private final SafetyCourseRepository courseRepository;

    /**
     * 근로자: 미이수 영상 목록 조회 (GET /uncompleted)
     */
    public List<SafetyCourseResponse> getUncompletedCourses(String userId) {

        // 1. 미이수된 courseId 목록 조회
        List<Integer> uncompletedCourseIds = enrollmentRepository.findUncompletedCourseIdsByUserId(userId);

        // 2. 해당 ID 목록으로 SafetyCourse 목록 조회
        List<SafetyCourse> courses = courseRepository.findAllByCourseIdIn(uncompletedCourseIds);

        // 3. DTO 변환 및 반환
        return courses.stream()
                .map(course -> new SafetyCourseResponse(
                        course.getCourseId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getVideoUrl(),
                        course.getDurationMinutes()
                ))
                .collect(Collectors.toList());
    }


    /**
     * 근로자: 교육 영상 이수 처리 (POST /complete/{videoId})
     */
    @Transactional
    public void completeCourse(String userId, Integer courseId) {

        // 1. 기존 이수 기록을 조회
        Optional<SafetyEnrollment> existingEnrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);

        if (existingEnrollment.isPresent()) {
            SafetyEnrollment enrollment = existingEnrollment.get();

            // 2. 이미 이수하지 않은 상태라면 업데이트
            if (!enrollment.getIsCompleted()) {
                enrollment.setIsCompleted(true);
                enrollment.setCompletedAt(LocalDateTime.now());
                enrollmentRepository.save(enrollment);
            }
        } else {
            // 3. 이수 기록이 없다면 새로 생성하며 바로 이수 처리 (첫 이수)
            SafetyEnrollment newEnrollment = SafetyEnrollment.builder()
                    .userId(userId)
                    .courseId(courseId)
                    .isCompleted(true)
                    .completedAt(LocalDateTime.now())
                    .build();

            enrollmentRepository.save(newEnrollment);
        }
    }
}