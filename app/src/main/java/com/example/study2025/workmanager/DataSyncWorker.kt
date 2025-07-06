package com.example.study2025.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DataSyncWorker(context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val repository = DataRepository()
        repository.fetchAndLogData()
        return Result.success()
    }
}