package com.example.study2025.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainThreadWorkingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //threadWorking()
        //main1()
        main2()
    }
}

/**
 * Multiple coroutines starting and resuming on different IO threads.
 */
fun main1() = runBlocking {
    repeat(100) { i ->
        launch(Dispatchers.IO) {
            println("Coroutine $i starting on ${Thread.currentThread().name}")
            delay(1000)  // Simulate work
            println("Coroutine $i resuming on ${Thread.currentThread().name}")
        }
    }
}


fun main2() = runBlocking {
    launch {
        repeat(5) {
            println("Coroutine A: $it on ${Thread.currentThread().name}")
            delay(100)
        }
    }

    launch {
        repeat(5) {
            println("Coroutine B: $it on ${Thread.currentThread().name}")
            delay(100)
        }
    }
}


fun threadWorking() {
    GlobalScope.launch(Dispatchers.Main) {
        println("Coroutine A: Starting")

        val result = withContext(Dispatchers.IO) {
            delay(1000)  // Simulating long task
            "Result Ready"
        }

        println("Coroutine A: Got result -> $result")
        repeat(5) {
            println("Coroutine A: Doing some work $it")
            delay(500)
        }
    }

    GlobalScope.launch(Dispatchers.Main) {
        repeat(5) {
            println("Coroutine B: Doing some work $it")
            delay(500)
        }
    }

}