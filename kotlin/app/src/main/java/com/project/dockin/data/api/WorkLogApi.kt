package com.project.dockin.data.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// 서버에서 내려주는 작업일지 DTO
data class WorkLogDto(
    val log_id: Int,
    val user_id: String,
    val equipment_id: Int?,
    val title: String,
    val log_text: String,
    val audio_file_url: String?,
    val created_at: String?,
    val updated_at: String?
)

// 생성용 요청 바디
data class CreateWorkLogReq(
    val title: String,
    val log_text: String,
    val equipment_id: Int? = null,
    val audio_file_url: String? = null
)

// 수정용 요청 바디
data class UpdateWorkLogReq(
    val title: String,
    val log_text: String,
    val equipment_id: Int? = null,
    val audio_file_url: String? = null
)

interface WorkLogApi {

    @GET("/api/work-logs")
    suspend fun list(): List<WorkLogDto>

    @POST("/api/work-logs")
    suspend fun create(@Body body: CreateWorkLogReq): WorkLogDto

    @PUT("/api/work-logs/{id}")
    suspend fun update(
        @Path("id") id: Int,
        @Body body: UpdateWorkLogReq
    ): WorkLogDto

    @DELETE("/api/work-logs/{id}")
    suspend fun delete(@Path("id") id: Int)
}