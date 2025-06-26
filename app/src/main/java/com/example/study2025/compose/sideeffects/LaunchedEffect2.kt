package com.example.study2025.compose.sideeffects

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LaunchedEffect2: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .systemBarsPadding()
                ) { innerPadding ->
                    CoroutineScopeComposable(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun CounterComposable(modifier: Modifier) {
        val counter = remember { mutableStateOf(0) }

        LaunchedEffect(key1 = Unit) { // coroutine scope
            Log.d("LaunchEffectComposable", "Started..")
            try {
                for(i in 1..10) {
                    counter.value++
                    delay(1000)
                }
            } catch(e: Exception) {
                Log.d("LaunchEffectComposable", e.message.toString())
            }
        }

 /*       Button(
            onClick = { LaunchedEffect(Unit) { } }
        ) { }*/

        var text = "Counter is running ${counter.value}"
        if(counter.value == 10) {
            text = "Counter stopped"
        }
        Text(text = text, modifier= modifier)
    }

    @Composable
    fun CoroutineScopeComposable(modifier: Modifier) {
        var counter = remember { mutableStateOf(0) }
        val scope = rememberCoroutineScope()

        var text = "Counter is running ${counter.value}"
        if(counter.value == 10) {
            text = "Counter Stopped"
        }
        Column(
            modifier = modifier
        ) {
            Text(text = text)
            Button(
                onClick = {
                    scope.launch {
                        Log.d("CoroutineScopeComposable", "Started..")
                        try {
                            for(i in 1..10) {
                                counter.value++
                                delay(1000)
                            }
                        } catch(e: Exception) {
                            Log.d("CoroutineScopeComposable", e.message.toString())
                        }
                    }
                }
            ) {
                Text(text = "Start")
            }
        }
    }
}













