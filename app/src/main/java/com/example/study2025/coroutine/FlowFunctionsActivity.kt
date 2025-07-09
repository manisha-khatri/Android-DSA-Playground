package com.example.study2025.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FlowFunctionsActivity: AppCompatActivity() {
    companion object {
        const val TAG = "CHEEZY_CODE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val job =
        GlobalScope.launch {
            val data: Flow<Int> = producer()
            data.collect {
                Log.d(TAG, it.toString())
            }
        }

        /*GlobalScope.launch {
            delay(3500)
            job.cancel()
        }*/

        GlobalScope.launch {
            val data: Flow<Int> = producer()
            delay(2500)
            data.collect {
                Log.d(TAG + "2", it.toString())
            }
        }
    }

    // Producer
    fun producer() = flow<Int> {
        val list = listOf(1,2,3,4,5,6,7,8,9,10)
        list.forEach {
            delay(1000)
            emit(it)
        }
    }

}