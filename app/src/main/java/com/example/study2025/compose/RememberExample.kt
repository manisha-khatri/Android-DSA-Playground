package com.example.study2025.compose

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
import androidx.compose.ui.Modifier
import com.example.composeapplication.ui.theme.ComposeApplicationTheme

class RememberExample: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .systemBarsPadding()
                ) { innerPadding ->
                    RememberCounter(
                        Modifier.padding(innerPadding)
                    )
                }
            }

        }
    }
}

/**
 * 🧪 CASE 1: Button with no value / no state
 */
@Composable
fun EmptyButton() {
    Button(onClick = {
        Log.d("tag", "Button clicked!")
    }) {
        Text("Click Me") // no recomposition happens.
    }
}

/**
 * 🧪 CASE 2: Button with regular counter increment (not state)
 */
@Composable
fun RegularCounter() {
    var count = 0

    Button(onClick = { count++ }) {
        Text("Clicked times $count") // prints 0 everytime, as onClick Compose recomposes and reset value to 0
    }
}


@Composable
fun RememberCounter(modifier: Modifier) {
    var count = remember { mutableStateOf(0) }

    Log.d("Tag", "counter updated - ${count.value}")

    Button(
        modifier = modifier,
        onClick = { count.value++
            Log.d("Tag", "counter updated onclick - ${count.value}")}
    ) {
        Log.d("Tag", "counter updated inside - ${count.value}")
        Text("Increase Counter ${count.value}") //When you click the button, count increases, and the UI updates with the new value.
    }
}