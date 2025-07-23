package com.example.study2025.coroutine

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.study2025.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutineScopeDoubts: AppCompatActivity() {
    lateinit var tv: TextView

    val _main = "inside Main"
    val _coroutineScope = "Inside coroutine Scope"
    val _lifecycleScope = "Inside lifecycle Scope"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById<TextView>(R.id.tv)
        val button = findViewById<Button>(R.id.btn)
        button.setOnClickListener {
            println("Button clicked!")  // Always prints instantly
        }

        lifecycleScope.launch {
            println(_main + "----> start")

            check_coroutineScope()

            //check_lifecycleScope()
            println(_main + "----> end")
        }
        println(_main + "----> outside of any coroutine")

        lifecycleScope.launch {
            println("Heyy I am new coroutine")
        }
    }

    override fun onResume() {
        super.onResume()
    }

    suspend fun check_coroutineScope() {
        coroutineScope {
            launch {
                delay(10000)
                println(_coroutineScope + " Task 1 done")
            }
            launch {
                delay(1500)
                println(_coroutineScope + " Task 2 done")
            }
        }
        println(_coroutineScope + " All tasks completed")
    }

    suspend fun check_CoroutineScope2() {
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                delay(1000)
                println(_coroutineScope + " Task 1 done")
            }
            launch {
                delay(500)
                println(_coroutineScope + " Task 2 done")
            }
        }
        println(_coroutineScope + " All tasks completed")
    }

    suspend fun check_lifecycleScope() {
        lifecycleScope.launch {
            launch {
                delay(1000)
                println(_lifecycleScope + " Task 1 done")
            }
            launch {
                delay(500)
                println(_lifecycleScope + " Task 2 done")
            }
        }
        println(_lifecycleScope + " All tasks completed")
    }
}

