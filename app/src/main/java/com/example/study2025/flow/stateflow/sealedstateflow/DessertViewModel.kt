package com.example.study2025.flow.stateflow.sealedstateflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DessertViewModel @Inject constructor(
    private val repository: DessertRepository
): ViewModel() {
    private val _desertState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val desertState: StateFlow<UiState<List<String>>> = _desertState

    init {
        viewModelScope.launch {
            delay(1000)
            try {
                val data = repository.getDesert()
                _desertState.value = UiState.Success(data)
            } catch (e: Exception) {
                _desertState.value = UiState.Failure(e.message.toString())
            }
        }
    }
}