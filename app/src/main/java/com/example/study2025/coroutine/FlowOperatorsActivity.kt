package com.example.study2025.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.coroutine.FlowFunctionsActivity.Companion.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class FlowOperatorsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            //terminal operators --> first(), toList(), collect()
            val res = producer().first() // Returns the first element
            Log.d(TAG, "first() = " + res.toString())

            val res2 = producer().toList() // Return the full list when done
            Log.d(TAG, "toList() = " + res2.toString())


            producer()
                .map {
                    delay(1000)
                    it *2
                    Log.d(TAG, "Map Thread - ${Thread.currentThread().name}")
                }// Everything above flowOn executes on the background thread
                .flowOn(Dispatchers.IO)
                .filter { it < 8 }
                .flowOn(Dispatchers.Main)
                .collect { // execute on Main Thread
                    Log.d(TAG, it.toString())
                }
        }
    }

    fun producer(): Flow<Int> = flow<Int> {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8,9,10,11,12)
        list.forEach {
            delay(500)
            Log.d(TAG, "Emitter Thread - ${Thread.currentThread().name}")
            emit(it)
        }
    }


}
