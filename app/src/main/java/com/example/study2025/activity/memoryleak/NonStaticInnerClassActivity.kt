package com.example.study2025.activity.memoryleak

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.R

class NonStaticInnerClassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Dangerous call
        val leaky = LeakyRunnable()
        Handler(Looper.getMainLooper()).postDelayed(leaky, 10000) // 10s delay

        val handler = Handler(Looper.getMainLooper())


        handler.postDelayed(
            object: Runnable {
                override fun run() {
                    TODO("Not yet implemented")
                }
        }, 10_000) // 10 seconds delay


    }


    inner class LeakyRunnable : Runnable {
        override fun run() {
            Toast.makeText(this@NonStaticInnerClassActivity, "Hello from the leak!", Toast.LENGTH_SHORT).show()
        }
    }

}