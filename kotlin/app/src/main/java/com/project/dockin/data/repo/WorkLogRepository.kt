package com.project.dockin.data.repo

import com.project.dockin.data.api.CreateWorkLogReq
import com.project.dockin.data.api.UpdateWorkLogReq
import com.project.dockin.data.api.WorkLogApi
import com.project.dockin.data.api.WorkLogDto

class WorkLogRepository(
    private val api: WorkLogApi
) {

    suspend fun fetch(): List<WorkLogDto> = api.list()

    suspend fun create(req: CreateWorkLogReq): WorkLogDto =
        api.create(req)

    suspend fun update(id: Int, req: UpdateWorkLogReq): WorkLogDto =
        api.update(id, req)

    suspend fun delete(id: Int) =
        api.delete(id)
}