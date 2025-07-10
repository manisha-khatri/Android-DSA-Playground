package com.example.study2025.coroutine.whycoroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity2: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val content = downloadFile("http://file")
            val processed = processFile(content)
            val result = saveToDatabase(processed)
            println(result)
        }

    }
}

suspend fun downloadFile(url: String): String { /*...*/ return "FileContent" }
suspend fun processFile(content: String): Boolean { /*...*/ return true }
suspend fun saveToDatabase(success: Boolean): String { /*...*/ return "Saved Successfully" }
