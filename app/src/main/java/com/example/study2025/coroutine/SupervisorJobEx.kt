package com.example.study2025.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job

class MainActivity7: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SupervisorJob
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

        scope.launch {
            throw RuntimeException("Oops!")  // This failure won't cancel others
        }
        scope.launch {
            delay(1000)
            println("I’m still alive!")
        }

        // Job
        val job = GlobalScope.launch {
            delay(3000)
            println("work is done!")
        }
        println(job.isActive)
        job.cancel()
        println(job.isCancelled)

        // Job()
        val scope2 = CoroutineScope(Dispatchers.IO + Job())

        scope2.launch {
            delay(5000)
            println("Task Completed")
        }
    }
}
