package com.example.DOCKin.repository;

import com.example.DOCKin.model.SafetyEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SafetyEnrollmentRepository extends JpaRepository<SafetyEnrollment, Integer> {

    // 사용자 ID와 교육 ID로 기존 이수 기록을 찾기 위한 메서드
    Optional<SafetyEnrollment> findByUserIdAndCourseId(String userId, Integer courseId);
}