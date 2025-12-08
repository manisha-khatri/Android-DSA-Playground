package com.example.study2025.compose.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LiveDataVM: ViewModel() {
    private val _count = MutableLiveData(0)
    val count: LiveData<Int> = _count

    fun increment() {
        _count.value = (_count.value ?: 0) + 1
    }
}

class StateFlowVM : ViewModel() {
    private val _state = MutableStateFlow(DataUiState())
    val state: StateFlow<DataUiState> = _state
}

data class DataUiState(
    val data:String = ""
)

