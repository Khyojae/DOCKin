package com.project.dockin.data.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SafetyApi {

    // 전체 교육 목록 조회: GET /api/safety/courses
    @GET("/api/safety/courses")
    suspend fun listCourses(): List<SafetyCourseDto>

    // 교육 이수 처리: POST /api/safety/enroll/{id}
    @POST("/api/safety/enroll/{id}")
    suspend fun enroll(
        @Path("id") courseId: Int
    ): EnrollResponse
}