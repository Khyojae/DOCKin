package com.project.dockin.data.dto

import com.squareup.moshi.Json

data class SafetyCourseDto(
    @Json(name = "courseId") val courseId: Int,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String?,
    @Json(name = "videoUrl") val videoUrl: String?,
    @Json(name = "durationMinutes") val durationMinutes: Int,
    @Json(name = "isMandatory") val isMandatory: Boolean = true,
    @Json(name = "createdAt") val createdAt: String?
)

data class EnrollResultDto(
    @Json(name = "enrollmentId") val enrollmentId: Int,
    @Json(name = "userId") val userId: String,
    @Json(name = "courseId") val courseId: Int,
    @Json(name = "isCompleted") val isCompleted: Boolean,
    @Json(name = "completedAt") val completedAt: String?
)