// WorkLogRepository.kt
package com.project.dockin.data.repo

import android.util.Log
import com.project.dockin.data.api.*

class WorkLogRepository(
    private val api: WorkLogApi
) {
    companion object {
        private const val TAG = "WorkLogRepo"
    }

    // 같은 구역 목록
    suspend fun fetch(): List<WorkLogDto> {
        return try {
            val res = api.list()
            Log.d(TAG, "fetch() success, size = ${res.size}")
            res
        } catch (e: Exception) {
            Log.e(TAG, "fetch() failed", e)
            emptyList()
        }
    }

    suspend fun getOne(logId: Long): WorkLogDto =
        api.getOne(logId)

    suspend fun create(req: CreateWorkLogReq): WorkLogDto =
        api.create(req)

    suspend fun update(id: Long, req: UpdateWorkLogReq): WorkLogDto =
        api.update(id, req)

    suspend fun delete(id: Long) {
        api.delete(id)
        Log.d(TAG, "delete($id) called")
    }
}