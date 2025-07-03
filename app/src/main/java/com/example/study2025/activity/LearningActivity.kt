package com.example.study2025.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.R

class LearningActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE)
        with(pref.edit()) {
            putBoolean("isLoggedIn", true)
            putString("user", "Manisha")
            apply()
        }

        val preff = getSharedPreferences("My_Pref", Context.MODE_PRIVATE)
        val isLoggedIn = preff.getBoolean("isLoggedIn", false)
        val user = preff.getString("user", "Default")
    }
}