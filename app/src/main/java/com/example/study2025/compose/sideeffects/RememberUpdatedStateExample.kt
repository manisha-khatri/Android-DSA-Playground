package com.example.study2025.compose.sideeffects

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import kotlinx.coroutines.delay

class RememberUpdatedStateExample: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .systemBarsPadding()
                ) { innerPadding ->
                    App(Modifier.padding(innerPadding))
                    AppAB()
                }
            }

        }
    }
}

@Composable
fun App(modifier: Modifier) {
    Text(
        text = "",
        modifier = modifier
    )
}


@Composable
fun Counter1() {
    var count = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(1000)
        count.value = 10
    }
    Counter4(count.value)
}

@Composable
fun Counter2(value: Int) {
    LaunchedEffect(Unit) {
        delay(1000)
        /**
         * Even after the value becomes 10, printing 0
         * --> Because when Counter1 calls Counter2 the value of count was 0
         * then recomposition happened --> rerun func --> value = 10
         * --> But cannot call Counter2.LaunchedEffect() because it can only be called once
         */
        Log.d("Logger", value.toString())
    }
    Text(text = value.toString())
}

/**
 * Getting the right output but the solution is wrong
 * LaunchedEffect is called 2 times + delay(1000)
 * Suppose delay() is a heavy operation doesn't wants to call it again and again
 */
@Composable
fun Counter3(value: Int) {
    LaunchedEffect(value) {
        delay(1000)
        Log.d("Logger", value.toString())
    }
    Text(text = value.toString())
}

@Composable
fun Counter4(value: Int) {
    val state = rememberUpdatedState(newValue = value)

    LaunchedEffect(Unit) {
        delay(2000)
        Log.d("Logger", state.value.toString())
    }
    Text(text = value.toString())
}

fun a() {
    Log.d("CHEEZYCODE", "I am A from App")
}

fun b() {
    Log.d("CHEEZYCODE", "I am B from App")
}

@Composable
fun AppAB() {
    var state = remember { mutableStateOf(::a) }

    Button(
        onClick = { state.value = ::b }
    ) {
        Text(text = "Click to change the state")
    }

    LandingScreen(state.value)
}

@Composable
fun LandingScreen(onTimeout: () -> Unit) {
    val currentTimeout = rememberUpdatedState(onTimeout)

    LaunchedEffect(true) {
        delay(5000)
        currentTimeout.value() // ✅ Call the latest lambda
    }
}












