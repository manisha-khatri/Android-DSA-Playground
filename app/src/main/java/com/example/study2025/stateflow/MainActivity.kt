package com.example.study2025.stateflow

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.study2025.R
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.counter_activity)

        val tv = findViewById<TextView>(R.id.counterTextView)
        val btn = findViewById<Button>(R.id.incrementButton)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.counter.collect { count ->
                    tv.text = count.toString()
                }
            }
        }

        btn.setOnClickListener {
            viewModel.increment()
        }
    }
}