package com.example.DOCKin.service;

import com.example.DOCKin.model.SafetyEnrollment;
import com.example.DOCKin.repository.SafetyEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SafetyEnrollmentService {

    private final SafetyEnrollmentRepository enrollmentRepository;

    // 1. 교육 이수 처리 (USER/ADMIN용)
    public SafetyEnrollment completeCourse(String userId, Integer courseId) {

        Optional<SafetyEnrollment> existingEnrollment =
                enrollmentRepository.findByUserIdAndCourseId(userId, courseId);

        SafetyEnrollment enrollment;

        if (existingEnrollment.isPresent()) {
            // 기존 기록이 있으면 업데이트 (재이수)
            enrollment = existingEnrollment.get();
            enrollment.setCompletedAt(LocalDateTime.now());
            enrollment.setIsCompleted(true);
        } else {
            // 새 기록 생성 (최초 이수)
            enrollment = SafetyEnrollment.builder()
                    .userId(userId)
                    .courseId(courseId)
                    .isCompleted(true)
                    .completedAt(LocalDateTime.now())
                    .build();
        }

        // 데이터베이스에 저장
        return enrollmentRepository.save(enrollment);
    }

    // 2. 사용자 이수 현황 조회 (관리자 ADMIN용)
    /**
     * 모든 근로자의 안전 교육 이수 기록을 조회합니다.
     */
    public List<SafetyEnrollment> findAllEnrollments() {
        // JpaRepository에서 제공하는 findAll() 메서드를 사용하여 모든 데이터를 가져옵니다.
        return enrollmentRepository.findAll();
    }
}