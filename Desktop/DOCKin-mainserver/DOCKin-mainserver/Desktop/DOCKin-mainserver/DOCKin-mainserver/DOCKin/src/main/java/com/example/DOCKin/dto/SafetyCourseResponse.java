package com.example.DOCKin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 교육 과정 정보를 담는 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SafetyCourseResponse {
    private Integer courseId;
    private String title;
    private String description;
    private String videoUrl;
    private Integer durationMinutes;
    // 이수 여부는 서비스에서 판단하여 별도로 전달할 수 있습니다.
}