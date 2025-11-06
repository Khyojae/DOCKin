package com.project.dockin.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendanceApi {
    @POST("api/attendance/in")
    suspend fun clockIn(@Body body: InReq): AttendanceRes
    data class InReq(val inLocation: String)

    @POST("api/attendance/out")
    suspend fun clockOut(@Body body: OutReq): AttendanceRes
    data class OutReq(val outLocation: String)

    @GET("api/attendance")
    suspend fun list(): List<AttendanceRes>
}

data class AttendanceRes(
    val id: Long,
    val userId: String,
    val clockInTime: String?,
    val clockOutTime: String?,
    val totalWorkTime: String?,
    val status: String,
    val inLocation: String?,
    val outLocation: String?
)