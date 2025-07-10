package com.example.study2025.compose.composetheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.composeapplication.ui.theme.ComposeApplicationTheme

class MainActivity5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Makes sure system bars (status/nav) are respected
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            ComposeApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding() // ✅ Adds padding for status & nav bars
                ) { innerPadding ->
                    App(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun App(name: String, modifier: Modifier = Modifier) {
    var theme = remember { mutableStateOf(false) }
    ComposeApplicationTheme(theme.value) {
        Column(modifier = modifier) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
            Button(
                onClick = {
                    theme.value = !theme.value
                }
            ) {
                Text(text= "Change Theme")
            }
        }
    }
}