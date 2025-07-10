package com.example.study2025.service.worker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.study2025.R

class MainActivity21: AppCompatActivity() {

    private val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doWork()
    }

    private fun doWork() {
        val request = OneTimeWorkRequest.Builder(DemoWorker::class.java)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        workManager.enqueue(request)

        workManager.getWorkInfoByIdLiveData(request.id).observe(this) {
            if(it != null) {
                printStatus(it.state.name)
            }
        }
    }

    private fun printStatus(name: String) {
        Log.d("CHEEZYCODE", "name = " + name)
    }
}
