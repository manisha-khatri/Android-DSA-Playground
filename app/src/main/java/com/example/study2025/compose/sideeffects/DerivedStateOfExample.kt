package com.example.study2025.compose.sideeffects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import kotlinx.coroutines.delay

class DerivedStateOfExample: ComponentActivity() {
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
                    //TextChange()
                    CalculateTable()
                }
            }
        }
    }

    @Composable
    fun App(modifiers: Modifier) {
        Text(modifier = modifiers, text = "")
    }

    @Composable
    fun TextChange() {
        //var text by remember { mutableStateOf("") }
        val text = remember { mutableStateOf("") }

        val isTextTooLong by remember {
            derivedStateOf { text.value.length > 10 }
        }

        Column {
            TextField(
                value = text.value,
                onValueChange = { text.value = it }
            )
            if(isTextTooLong) {
                Text("Text is too long!!")
            }
        }
    }

    @Composable
    fun CalculateTable() {
        val table = remember { mutableStateOf(5) }

        val index by produceState(initialValue = 0, key1 = table.value) {
            for (i in 0..10) {
                value = i
                delay(1000L)
            }
        }

        val result by remember(table.value, index) {
            derivedStateOf {
                "${table.value} * $index = ${table.value * index}"
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = result,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}