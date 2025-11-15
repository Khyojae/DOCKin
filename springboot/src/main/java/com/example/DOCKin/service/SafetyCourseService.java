package com.example.DOCKin.service;
import com.example.DOCKin.dto.CourseCreateRequest;
import com.example.DOCKin.model.SafetyCourse;
import com.example.DOCKin.repository.SafetyCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SafetyCourseService {

    private final SafetyCourseRepository courseRepository;

    /**
     * 관리자: 새로운 안전 교육 코스를 등록합니다.
     * @param request 교육 생성 요청 DTO
     * @param adminId 등록을 요청한 관리자 ID (현재 로그인 사용자)
     * @return 등록된 교육 엔티티
     */
    @Transactional
    public SafetyCourse createCourse(CourseCreateRequest request, String adminId) {
        SafetyCourse newCourse = SafetyCourse.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .materialUrl(request.getMaterialUrl())
                .durationMinutes(request.getDurationMinutes())
                .createdBy(adminId)
                .build();

        return courseRepository.save(newCourse);
    }

    /**
     * 전체 교육 목록을 조회합니다.
     * @return 전체 SafetyCourse 목록
     */
    public List<SafetyCourse> findAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * 특정 교육의 상세 정보를 조회합니다.
     * @param courseId 조회할 교육 ID
     * @return SafetyCourse 엔티티
     */
    public SafetyCourse findCourseById(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + courseId + "인 교육 코스를 찾을 수 없습니다."));
    }

    /**
     * 관리자: 교육 자료를 수정합니다.
     * @param courseId 수정할 교육 ID
     * @param request 수정 요청 DTO
     * @return 수정된 교육 엔티티
     */
    @Transactional
    public SafetyCourse updateCourse(Integer courseId, CourseCreateRequest request) {
        SafetyCourse course = findCourseById(courseId); // 먼저 교육을 찾고

        // 엔티티 내부의 update 메서드를 호출하여 변경 감지(Dirty Checking)로 자동 업데이트
        course.update(request.getTitle(), request.getDescription(), request.getMaterialUrl(), request.getDurationMinutes());

        // save()를 명시적으로 호출하지 않아도 @Transactional에 의해 커밋 시점에 업데이트됩니다.
        return course;
    }

    /**
     * 관리자: 교육 자료를 삭제합니다.
     * @param courseId 삭제할 교육 ID
     */
    @Transactional
    public void deleteCourse(Integer courseId) {
        // 실제 프로젝트에서는 foreign key가 걸린 safety_enrollments의 데이터도 먼저 삭제해야 합니다.
        courseRepository.deleteById(courseId);
    }
}