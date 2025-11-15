// WorkLogApi.kt
package com.project.dockin.data.api

import retrofit2.http.*

interface WorkLogApi {

    // 같은 구역 작업일지 목록
    @GET("/api/work-logs")
    suspend fun list(): List<WorkLogDto>

    // 단일 조회 (혼자보기)
    @GET("/api/work-logs/{logId}")
    suspend fun getOne(
        @Path("logId") logId: Long
    ): WorkLogDto

    @POST("/api/work-logs")
    suspend fun create(
        @Body req: CreateWorkLogReq
    ): WorkLogDto

    @PUT("/api/work-logs/{logId}")
    suspend fun update(
        @Path("logId") logId: Long,
        @Body req: UpdateWorkLogReq
    ): WorkLogDto

    @DELETE("/api/work-logs/{logId}")
    suspend fun delete(
        @Path("logId") logId: Long
    )
}