package com.example.study2025.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.study2025.coroutine.FlowFunctionsActivity.Companion.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StateFlowEx: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            val result = producers()
            delay(6500)
            result.collect {
                Log.d(TAG, "Item - $it")
            }
        }
    }

    private fun producers(): Flow<Int> {
        val mutableStateFlow = MutableStateFlow<Int>(10)

        GlobalScope.launch {
            delay(2000)
            mutableStateFlow.emit(20)
            delay(2000)
            mutableStateFlow.emit(30)
        }
        return mutableStateFlow
    }
}

class MyViewModel : ViewModel() {

    private val _counter = MutableStateFlow(0)   // Mutable for internal updates
    val counter: StateFlow<Int> = _counter       // Expose as read-only

    fun incrementCounter() {
        _counter.value = _counter.value + 1
    }
}
