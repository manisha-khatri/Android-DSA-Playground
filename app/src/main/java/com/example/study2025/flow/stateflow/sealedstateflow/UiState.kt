package com.example.study2025.flow.stateflow.sealedstateflow

sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    class Failure(val message: String): UiState<Nothing>()
}
