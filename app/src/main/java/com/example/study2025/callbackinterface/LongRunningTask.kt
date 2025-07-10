package com.example.study2025.callbackinterface

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.study2025.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.lang.ref.WeakReference

interface DownloadCallback {
    fun onDownloadComplete(message: String)
}

class LongRunningTask(private val activity: DownloadCallback) {
    private val callbackRef = WeakReference(activity) // Avoid memory leak

    fun startDownload() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(1000)
            callbackRef.get()?.onDownloadComplete("Download Finished")
        }
    }
}

class MainActivity4: AppCompatActivity(), DownloadCallback {
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById<TextView>(R.id.tv)

        val task = LongRunningTask(this)
        task.startDownload()
    }

    override fun onDownloadComplete(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            tv.text = message
        }
    }
}