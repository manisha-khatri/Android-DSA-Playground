package com.example.study2025.service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.study2025.R

class MainActivity19 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_service)

        val startButton: Button = findViewById(R.id.btnStart)
        val stopButton: Button = findViewById(R.id.btnStop)

        startButton.setOnClickListener {
            val intent = Intent(this, LoggerService::class.java)
            //startService(intent)
            ContextCompat.startForegroundService(this, intent)
        }

        stopButton.setOnClickListener {
            val intent = Intent(this, LoggerService::class.java)
            stopService(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        requestNotificationPermission()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }

}
