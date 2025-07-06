package com.example.study2025.workmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.databinding.ActivityWorkManagerBinding

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWorkManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tv.text = "WorkManager will fetch data every 20 minutes.\nCheck Logcat."
    }
}