package com.project.dockin.data.api
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginReq(val userId: String, val password: String)
data class LoginRes(val token: String)

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body body: LoginReq): LoginRes
}