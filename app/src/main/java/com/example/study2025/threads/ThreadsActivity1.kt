package com.example.study2025.threads

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.study2025.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ThreadActivity1 : AppCompatActivity() {

    private var myThread: Thread? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //thread1()
        //handlerThread()
        startOldExampleThread()
    }

    override fun onStart() {
        super.onStart()
        myThread = Thread {
            // Background thread
            runOnUiThread {
                // Main Thread
            }
        }
        myThread?.start()
    }

    override fun onStop() {
        super.onStop()
        myThread?.interrupt()
    }

    fun startOldExampleThread() {
        val exampleThread = ExampleThread()
        exampleThread.start() // Starts the thread and its Looper

        // Give it a tiny delay to ensure the Handler is initialized
        Handler(Looper.getMainLooper()).postDelayed({
            exampleThread.sendMessage("Hello from Main Thread!")
        }, 100)
    }

    fun justHandler() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            println("Handler task running!!")
        }, 1000)
    }


    fun thread1() {
        // Plain Thread (Background Thread)
        val thread = Thread(Runnable {
            println("I am working!!")
        })
        thread.start()

        // One-Time Delayed Task: Main Thread
        val handler = Handler(Looper.getMainLooper()) // talks to the main thread
        val runnable = Runnable {
            println("Hello after some seconds!")
        }
        handler.postDelayed(runnable, 1000) // run this after 1 seconds


        // Repeating Task (Looping Runnable): Main Thread
        val handler1 = Handler(Looper.getMainLooper())
        val runnable1 = object : Runnable {
            override fun run() {
                println("Hello after some seconds!")
                handler1.postDelayed(this, 1000) // run again after 1 second
            }
        }
        handler1.post(runnable1) // start the loop

        handler1.removeCallbacks(runnable1)

        // will run only 1 time

        val runnable2 = object : Runnable {
            override fun run() {
                Toast.makeText(this@ThreadActivity1, "Still here!", Toast.LENGTH_SHORT).show()
            }
        }
        handler.postDelayed(runnable2, 10_000)


        // Coroutine: uses the Main thread dispatcher by default
        lifecycleScope.launch {
            while (isActive) {
                println("Hello after some seconds!")
                delay(1000)
            }
        }
    }

    fun handlerThread() {
        val handlerThread = HandlerThread("My Thread")
        handlerThread.start()

        val handler3 = Handler(handlerThread.looper)
        handler3.post {
            println("Task running on the background thread!")
        }
    }
}

class ExampleThread : Thread() {
    private lateinit var handler: Handler

    override fun run() {
        Looper.prepare() // Prepare the looper for this thread

        handler = Handler(Looper.myLooper()!!) { message ->
            val value = message.obj as? String
            println("Message received: $value")
            true
        }

        Looper.loop() // Start looping for messages
    }

    fun sendMessage(value: String) {
        val message = handler.obtainMessage()
        message.obj = value  // Pass any object (here, a String)
        handler.sendMessage(message)
    }
}



