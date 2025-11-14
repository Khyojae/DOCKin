package com.project.dockin.data.api

// /api/safety/courses 응답 1건
data class SafetyCourseDto(
    val courseId: Int,
    val title: String,
    val description: String?,
    val videoUrl: String?,
    val durationMinutes: Int,
    val isMandatory: Boolean,
    val createdAt: String?
)

// /api/safety/enroll/{id} 응답 (형식은 팀원이랑 맞춰도 됨)
data class EnrollResponse(
    val success: Boolean,
    val message: String?
)