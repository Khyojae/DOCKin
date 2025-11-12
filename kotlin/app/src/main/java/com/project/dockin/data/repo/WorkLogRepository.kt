package com.project.dockin.data.repo

import com.project.dockin.data.api.CreateWorkLogReq
import com.project.dockin.data.api.UpdateWorkLogReq
import com.project.dockin.data.api.WorkLogApi
import com.project.dockin.data.api.WorkLogDto

class WorkLogRepository(private val api: WorkLogApi) {
    suspend fun fetch(): List<WorkLogDto> = api.list()
    suspend fun detail(id: Int) = api.get(id)
    suspend fun create(title: String, text: String, equipmentId: Int?) =
        api.create(CreateWorkLogReq(title, text, equipmentId))
    suspend fun update(id: Int, title: String, text: String) =
        api.update(id, UpdateWorkLogReq(title, text))
    suspend fun delete(id: Int) = api.delete(id)
}