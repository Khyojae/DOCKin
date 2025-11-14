package com.project.dockin.data.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// ───────────────── DTO들 ─────────────────

// safety_courses 테이블 매핑
data class SafetyCourseDto(
    val courseId: Int,          // course_id
    val title: String,
    val description: String?,   // NULL 가능
    val videoUrl: String,       // video_url
    val durationMinutes: Int,   // duration_minutes
    val isMandatory: Boolean,   // is_mandatory
    val createdAt: String?      // created_at (문자열로 받아도 됨)
)

// /api/safety/enroll/{id} 응답용 (간단하게)
data class EnrollResponse(
    val success: Boolean,
    val message: String?
)

// (관리자 이수 현황 조회용 – 나중에 쓸 수도 있으니 기본 형태만)
// safety_enrollments + users 조인해서 내려준다고 가정
data class SafetyEnrollmentDto(
    val enrollmentId: Int,
    val userId: String,
    val courseId: Int,
    val isCompleted: Boolean,
    val completionDate: String?
)

interface SafetyApi {

    // 전체 교육 목록 조회
    // GET /api/safety/courses
    @GET("/api/safety/courses")
    suspend fun listCourses(): List<SafetyCourseDto>

    // 특정 교육 상세 조회
    // GET /api/safety/courses/{courseId}
    @GET("/api/safety/courses/{courseId}")
    suspend fun getCourse(
        @Path("courseId") courseId: Int
    ): SafetyCourseDto

    // 교육 이수 처리
    // POST /api/safety/enroll/{id}
    @POST("/api/safety/enroll/{id}")
    suspend fun enroll(
        @Path("id") courseId: Int
    ): EnrollResponse

    // (관리자) 사용자 이수 현황 조회
    // GET /api/safety/admin/enrollments
    @GET("/api/safety/admin/enrollments")
    suspend fun listEnrollments(): List<SafetyEnrollmentDto>
}