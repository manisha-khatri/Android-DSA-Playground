package com.example.study2025.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SharedFlows: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            val result = producer()
            result.collect {
                Log.d(FlowFunctionsActivity.TAG, "Item 1 - $it")
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            val result = producer()
            delay(2500)
            result.collect {
                Log.d(FlowFunctionsActivity.TAG, "Items 2 - $it")
            }
        }
    }

    private fun producer(): Flow<Int> {
        val mutableSharedFlow = MutableSharedFlow<Int>(replay = 2) // replays last value

        GlobalScope.launch {
            val list = listOf(1,2,3,4,5,6)
            list.forEach{
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }

/*    fun producer(): Flow<Int> = flow<Int> {
        val list = listOf(1,2,3,4,5,6,7,8,9,10)
        list.forEach{
            delay(1000)
            emit(it)
        }
    }*/


}