package com.example.study2025.compose.sideeffects

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import kotlinx.coroutines.delay

class ProduceStateEx: ComponentActivity() {
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
                    //Counters()
                    //ProduceStateCounter()
                    //MyScreen()
                    Loader()
                }
            }

        }
    }

    @Composable
    fun App(modifiers: Modifier) {
        Text(modifier = modifiers, text = "")
    }

    @Composable
    fun Counters() {
        val state = remember{mutableStateOf(0)}
        LaunchedEffect(Unit) {
            for(i in 1..10) {
                delay(1000)
                state.value++
            }
        }

        Text(
            text = state.value.toString(),
            style = MaterialTheme.typography.titleMedium
        )
    }

    @Composable
    fun ProduceStateCounter() {
        val state = produceState(initialValue = 0) {
            for(i in 1..10) {
                delay(1000)
                value ++
            }
        }

        Text(
            text = state.value.toString(),
            style = MaterialTheme.typography.titleMedium
        )
    }

    @Composable
    fun MyScreen() {
        val message by produceState(initialValue = "Loading...") {
            // wait for 2 seconds (fake data loading)
            delay(2000)
            // once done, update the value
            value = "Data is loaded!"
        }

        Text(text = message)  // UI will update automatically when value changes
    }

    @Composable
    fun Loader() {
        val degree = produceState(initialValue = 0) {
            while (true) {
                delay(16)
                value = (value + 10) % 360
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(1f),
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "",
                        modifier = Modifier
                            .size(60.dp)
                            .rotate(degree.value.toFloat())
                    )
                    Text(text = "Loading...")
                }
            }
        )
    }







































}