package com.example.study2025.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity14: AppCompatActivity() {

    private val observer = MyObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(observer)
    }
}