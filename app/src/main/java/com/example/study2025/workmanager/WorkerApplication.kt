package com.example.study2025.workmanager

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
/*

class WorkerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleOneTimeWork()  // <-- Use this for immediate testing
        // schedulePeriodicWork() // <-- Keep this for actual production periodic work
    }

    private fun scheduleOneTimeWork() {
        val oneTimeWork = OneTimeWorkRequestBuilder<DataSyncWorker>().build()
        WorkManager.getInstance(this).enqueue(oneTimeWork)
    }

    private fun schedulePeriodicWork() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(15, TimeUnit.MINUTES) // Minimum allowed
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DataSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}
*/
