package com.project.dockin.ui.worklog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dockin.data.api.CreateWorkLogReq
import com.project.dockin.data.api.UpdateWorkLogReq
import com.project.dockin.data.api.WorkLogDto
import com.project.dockin.data.repo.WorkLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkLogViewModel(
    private val repo: WorkLogRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<WorkLogDto>>(emptyList())
    val items: StateFlow<List<WorkLogDto>> = _items

    /** 목록 새로고침 */
    fun refresh() = viewModelScope.launch {
        runCatching { repo.fetch() }
            .onSuccess { _items.value = it }
            .onFailure {
                // TODO: 에러 핸들링 필요하면 여기
            }
    }

    /** 생성 */
    fun create(
        title: String,
        logText: String,
        equipmentId: Int? = null,
        audioUrl: String? = null,
        onDone: (Boolean, String?) -> Unit = { _, _ -> }
    ) = viewModelScope.launch {
        val req = CreateWorkLogReq(
            title = title,
            log_text = logText,
            equipment_id = equipmentId,
            audio_file_url = audioUrl
        )
        runCatching { repo.create(req) }
            .onSuccess {
                refresh()
                onDone(true, null)
            }
            .onFailure { e ->
                onDone(false, e.message)
            }
    }

    /** 수정 (지금은 안 써도 되고, 나중에 디테일/수정 화면 만들 때 사용) */
    fun update(
        logId: Int,
        title: String,
        logText: String,
        equipmentId: Int? = null,
        audioUrl: String? = null,
        onDone: (Boolean, String?) -> Unit = { _, _ -> }
    ) = viewModelScope.launch {
        val req = UpdateWorkLogReq(
            title = title,
            log_text = logText,
            equipment_id = equipmentId,
            audio_file_url = audioUrl
        )
        runCatching { repo.update(logId, req) }
            .onSuccess {
                refresh()
                onDone(true, null)
            }
            .onFailure { e ->
                onDone(false, e.message)
            }
    }

    /** 삭제 */
    fun remove(
        logId: Int,
        onDone: (Boolean, String?) -> Unit = { _, _ -> }
    ) = viewModelScope.launch {
        runCatching { repo.delete(logId) }
            .onSuccess {
                refresh()
                onDone(true, null)
            }
            .onFailure { e ->
                onDone(false, e.message)
            }
    }
}