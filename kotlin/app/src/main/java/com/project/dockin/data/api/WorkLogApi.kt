package com.project.dockin.data.api

import retrofit2.http.*

data class WorkLogDto(
    val log_id: Int,
    val user_id: String,
    val equipment_id: Int?,
    val title: String,
    val log_text: String,
    val created_at: String?,
    val updated_at: String?
)

data class CreateWorkLogReq(
    val title: String,
    @JvmField val log_text: String,
    val equipment_id: Int? = null
)

data class UpdateWorkLogReq(
    val title: String,
    val log_text: String
)

interface WorkLogApi {
    @GET("/api/work-logs")
    suspend fun list(): List<WorkLogDto>

    @GET("/api/work-logs/{id}")
    suspend fun get(@Path("id") id: Int): WorkLogDto

    @POST("/api/work-logs")
    suspend fun create(@Body body: CreateWorkLogReq): WorkLogDto

    @PUT("/api/work-logs/{id}")
    suspend fun update(@Path("id") id: Int, @Body body: UpdateWorkLogReq): WorkLogDto

    @DELETE("/api/work-logs/{id}")
    suspend fun delete(@Path("id") id: Int)
}