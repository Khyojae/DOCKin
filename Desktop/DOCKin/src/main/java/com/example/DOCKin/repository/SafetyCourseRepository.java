package com.example.DOCKin.repository;

import com.example.DOCKin.model.SafetyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafetyCourseRepository extends JpaRepository<SafetyCourse, Integer> {

    // ⭐ 추가된 메서드: ID 목록으로 Course 엔티티 목록을 조회합니다.
    List<SafetyCourse> findAllByCourseIdIn(List<Integer> courseIds);
}