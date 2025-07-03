package com.example.study2025.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.study2025.R
import kotlin.concurrent.thread

class LoggerService: Service() {

    private var isRunning = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Log.d("LoggerService", "onCreate()")
        createNotificationChannel()
        startForeground(111, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("LoggerService", "onStartCommand()")

        Thread {
            while(isRunning) {
                Log.d("LoggerService", "Logging message..")
                Thread.sleep(1000)
            }
        }.start()
        //stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    fun getPendingIntent(): PendingIntent { //On click of activity redirect to main activity
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "ID",
                "CHEEZYCODE Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "ID")
            .setContentTitle("CHEEZYCODE")
            .setContentText("Foreground Service Running..")
            .setSmallIcon(R.drawable.heart2)
            .setContentIntent(getPendingIntent())
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        Log.d("LoggerService", "onDestroy()")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}