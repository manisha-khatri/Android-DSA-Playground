package com.example.study2025.flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

class FlowOperatorsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            //terminal operators --> first(), toList(), collect()
            val res = producer().first() // Returns the first element
            Log.d(FlowFunctionsActivity.Companion.TAG, "first() = " + res.toString())

            val res2 = producer().toList() // Return the full list when done
            Log.d(FlowFunctionsActivity.Companion.TAG, "toList() = " + res2.toString())


            producer()
                .map {
                    delay(1000)
                    it *2
                    Log.d(FlowFunctionsActivity.Companion.TAG, "Map Thread - ${Thread.currentThread().name}")
                }// Everything above flowOn executes on the background thread
                .flowOn(Dispatchers.IO)
                .filter { it < 8 }
                .flowOn(Dispatchers.Main)
                .collect { // execute on Main Thread
                    Log.d(FlowFunctionsActivity.Companion.TAG, it.toString())
                }
        }
    }

    fun producer(): Flow<Int> = flow<Int> {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8,9,10,11,12)
        list.forEach {
            delay(500)
            Log.d(FlowFunctionsActivity.Companion.TAG, "Emitter Thread - ${Thread.currentThread().name}")
            emit(it)
        }
    }
}