package com.project.dockin.data.repo

import com.project.dockin.data.api.ArApi

class ArRepository(
    private val api: ArApi
) {
    suspend fun getMemos(equipmentId: Int) =
        api.getMemos(equipmentId)

    suspend fun saveMemo(equipmentId: Int, text: String) =
        api.saveMemo(ArApi.SaveMemoRequest(equipmentId, text))

    suspend fun deleteMemo(memoId: Long) =
        api.deleteMemo(memoId)

    suspend fun getRoute(from: String, to: String) =
        api.getRoute(from, to)
}