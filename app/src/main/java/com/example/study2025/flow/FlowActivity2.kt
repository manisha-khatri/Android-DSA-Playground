package com.example.study2025.flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.onEach

class FlowActivity2: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            producer()
                .onStart {
                    emit(-1)
                    Log.d(FlowFunctionsActivity.TAG, "Starting out!")
                }
                .onCompletion {
                    emit(6)
                    Log.d(FlowFunctionsActivity.TAG, "completed!!")
                }
                .onEach {
                    Log.d(FlowFunctionsActivity.TAG, "About to emit - $it")
                }
                .collect {
                    Log.d(FlowFunctionsActivity.TAG, "${it}")
                }
        }

    }

    // Producer
    fun producer(): Flow<Int> = flow<Int> {
        val list = listOf(1,2,3,4,5,6,7,8,9,10)
        list.forEach{
            delay(1000)
            emit(it)
        }
    }

}