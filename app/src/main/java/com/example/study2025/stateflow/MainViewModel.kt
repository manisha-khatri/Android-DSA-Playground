package com.example.study2025.stateflow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {
    private val _counter = MutableStateFlow<Int>(0)
    val counter: StateFlow<Int> = _counter

    fun increment() {
        _counter.value += 1
    }


}