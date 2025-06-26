package com.example.study2025.compose.sideeffects

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import java.util.Timer
import java.util.TimerTask

class DisposableEffectExample: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            TimerComponent()
        }
    }
}

@Composable
fun TimerComponent() {
   DisposableEffect(Unit) {
       val timer = Timer()
       timer.schedule(object : TimerTask() {
           override fun run() {
               Log.d("Timer", "Tick")
           }
       }, 0, 1000)

       onDispose {
           Log.d("Timer", "Stopped") // On closing the app
           timer.cancel()
       }
   }
}
