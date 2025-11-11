package com.example.DOCKin.repository;

import com.example.DOCKin.model.SafetyEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SafetyEnrollmentRepository extends JpaRepository<SafetyEnrollment, Integer> {

    // [기존 코드 유지]
    // 사용자 ID와 교육 ID로 기존 이수 기록을 찾기 위한 메서드
    Optional<SafetyEnrollment> findByUserIdAndCourseId(String userId, Integer courseId);

    // -----------------------------------------------------------------
    // [새로운 코드 추가]
    // 특정 사용자의 미이수 교육 과정(SafetyCourse) 목록을 조회하는 쿼리
    @Query("SELECT se.courseId FROM SafetyEnrollment se WHERE se.userId = :userId AND se.isCompleted = FALSE")
    List<Integer> findUncompletedCourseIdsByUserId(@Param("userId") String userId);
}