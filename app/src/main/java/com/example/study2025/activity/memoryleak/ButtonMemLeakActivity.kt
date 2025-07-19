package com.example.study2025.activity.memoryleak

import android.annotation.SuppressLint
import com.example.study2025.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ButtonMemLeakActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memory_leak_btn)

        val button: Button = findViewById<Button>(R.id.button1)
        button.setOnClickListener {
            println("Button clicked!")
        }

        SomeSingleton.setListener(button) // BIG mistake
    }
}

@SuppressLint("StaticFieldLeak")
object SomeSingleton {
    var listener: View? = null  // now it matches
    fun setListener(l: View) {
        listener = l
    }
}