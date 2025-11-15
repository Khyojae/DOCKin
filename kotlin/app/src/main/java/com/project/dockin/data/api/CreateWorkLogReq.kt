package com.project.dockin.data.api

data class CreateWorkLogReq(
    val title: String,
    val log_text: String,
    // 같은 구역 필터용 장비 id (없으면 null)
    val equipment: Long? = null
)