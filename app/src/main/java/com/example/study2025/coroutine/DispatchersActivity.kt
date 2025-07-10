package com.example.study2025.coroutine

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.study2025.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DispatchersActivity: AppCompatActivity() {
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainDispatcher()
        ioDispatcher()
        defaultDispatcher()

    }

    fun mainDispatcher() {
        lifecycleScope.launch(Dispatchers.Main) {
            tv.text = "hello world"
        }
    }

    fun ioDispatcher() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = longRunningTask()
            withContext(Dispatchers.Main) {
                tv.text = response
            }
        }
    }

    fun defaultDispatcher() {
        lifecycleScope.launch(Dispatchers.Default) {
            val response = longRunningTask()
            withContext(Dispatchers.Main) {
                tv.text = response
            }
        }
    }

    private suspend fun longRunningTask(): String {
        delay(1000)
        return "Task Finished!"
    }
}

