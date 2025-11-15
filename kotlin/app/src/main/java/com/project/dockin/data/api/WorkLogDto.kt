// WorkLogDto.kt
package com.project.dockin.data.api

data class WorkLogDto(
    val log_id: Long,
    val user_id: String,
    val equipment_id: Long?,   // null 가능
    val title: String,
    val log_text: String,
    val created_at: String,
    val updated_at: String
)

