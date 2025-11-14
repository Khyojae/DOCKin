package com.project.dockin.ui.ar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.project.dockin.data.api.ArApi
import com.project.dockin.data.repo.ArRepository

class ArMemoViewModel(
    private val repo: ArRepository
) : ViewModel() {

    private val _memos =
        MutableStateFlow<List<ArApi.ArMemoDto>>(emptyList())
    val memos: StateFlow<List<ArApi.ArMemoDto>> = _memos

    fun loadMemos(equipmentId: Int) {
        viewModelScope.launch {
            runCatching { repo.getMemos(equipmentId) }
                .onSuccess { _memos.value = it }
                .onFailure { /* TODO: 에러 처리 */ }
        }
    }

    fun addMemo(equipmentId: Int, text: String) {
        viewModelScope.launch {
            runCatching { repo.saveMemo(equipmentId, text) }
                .onSuccess { newMemo ->
                    _memos.value = _memos.value + newMemo
                }
        }
    }

    fun deleteMemo(memoId: Long) {
        viewModelScope.launch {
            runCatching { repo.deleteMemo(memoId) }
                .onSuccess {
                    _memos.value =
                        _memos.value.filterNot { it.memoId == memoId }
                }
        }
    }
}