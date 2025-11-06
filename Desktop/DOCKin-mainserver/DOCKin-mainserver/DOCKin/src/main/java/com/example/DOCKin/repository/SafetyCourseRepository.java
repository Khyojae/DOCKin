// SafetyCourseRepository.java 파일의 최상단
package com.example.DOCKin.repository;

import com.example.DOCKin.model.SafetyCourse; // SafetyCourse 엔티티 import 필요
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafetyCourseRepository extends JpaRepository<SafetyCourse, Integer> {
    // JpaRepository 기본 메서드: save(), findById(), findAll(), deleteById() 등을 상속받아 사용합니다.
}