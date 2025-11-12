package com.project.dockin.data.api

import com.project.dockin.data.dto.EnrollResultDto
import com.project.dockin.data.dto.SafetyCourseDto
import retrofit2.http.*

interface SafetyApi {
    @GET("/api/safety/courses")
    suspend fun listCourses(): List<SafetyCourseDto>

    @GET("/api/safety/courses/{id}")
    suspend fun courseDetail(@Path("id") id: Int): SafetyCourseDto

    // 근로자 이수 처리
    @POST("/api/safety/enroll/{id}")
    suspend fun enroll(@Path("id") courseId: Int): EnrollResultDto
}