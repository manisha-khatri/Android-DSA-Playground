package com.example.study2025.unittests.notesapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.R

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val tvNote = findViewById<TextView>(R.id.tvNote)

        val key = intent.getStringExtra("KEY")

        tvNote.text = key
    }
}
