package com.project.dockin.data.api

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AiApi {

    // STT
    data class SttResponse(
        val text: String,
        val provider: String?
    )

    @Multipart
    @POST("/api/worklogs/stt")
    suspend fun requestStt(
        @Part file: MultipartBody.Part
    ): SttResponse

    // 번역
    data class TranslateRequest(
        val text: String,
        val source: String,
        val target: String,
        val traceId: String? = null
    )

    data class TranslateResponse(
        val translated: String,
        val model: String?,
        val traceId: String?
    )

    @POST("/api/translate")
    suspend fun translate(
        @Body body: TranslateRequest
    ): TranslateResponse

    // 챗봇
    data class ChatMessage(
        val role: String,   // "user" / "assistant"
        val content: String
    )

    data class ChatRequest(
        val messages: List<ChatMessage>,
        val domain: String = "shipyard",
        val lang: String = "ko",
        val traceId: String? = null
    )

    data class ChatResponse(
        val reply: String,
        val model: String?,
        val usage: Map<String, Any>?,
        val traceId: String?
    )

    @POST("/api/chat")
    suspend fun chat(
        @Body body: ChatRequest
    ): ChatResponse
}