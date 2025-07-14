package com.example.study2025.unittests.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.R

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()

            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra("title", title)
                putExtra("description", description)
            }
            startActivity(intent)
        }
    }
}
