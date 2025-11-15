// WorkLogViewModel.kt
package com.project.dockin.ui.worklog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dockin.data.api.WorkLogDto
import com.project.dockin.data.api.UpdateWorkLogReq
import com.project.dockin.data.repo.WorkLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkLogViewModel(
    private val repo: WorkLogRepository
) : ViewModel() {

    companion object {
        private const val TAG = "WorkLogVM"
    }

    private val _items = MutableStateFlow<List<WorkLogDto>>(emptyList())
    val items: StateFlow<List<WorkLogDto>> = _items

    fun refresh() {
        viewModelScope.launch {
            Log.d(TAG, "refresh() called")
            val list = repo.fetch()
            Log.d(TAG, "refresh() result size = ${list.size}")
            _items.value = list
        }
    }

    fun remove(id: Long, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                repo.delete(id)
                // 삭제 성공 후 다시 목록 불러오기
                refresh()
                callback(true, null)
            } catch (e: Exception) {
                Log.e(TAG, "remove($id) failed", e)
                callback(false, e.message)
            }
        }
    }
}