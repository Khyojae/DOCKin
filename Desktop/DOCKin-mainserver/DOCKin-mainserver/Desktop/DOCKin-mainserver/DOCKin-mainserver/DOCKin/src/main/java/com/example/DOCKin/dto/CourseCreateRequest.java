package com.example.DOCKin.dto;
import lombok.Getter;

@Getter
public class CourseCreateRequest {
    private String title;
    private String description;
    private String materialUrl;
    private Integer durationMinutes;
    // createdBy는 보통 로그인한 사용자 정보에서 가져오므로 Request에는 포함하지 않거나, 관리자 ID를 명시할 수 있습니다.
}