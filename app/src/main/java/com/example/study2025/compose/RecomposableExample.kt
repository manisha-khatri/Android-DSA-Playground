package com.example.study2025.compose

import android.R.attr.onClick
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class RecomposableExample: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposableButton()
        }
    }

    @Composable
    fun ComposableButton(){
        val state = remember { mutableStateOf(0.0) }
        Log.d("Tagged", "Logged during initial composition")
        Button(
            onClick = {
                state.value = Math.random()
            }
        ) {
            Log.d("Tagged", "Logged during composition and recomposition")
            Text(text = state.value.toString())
        }

    }
}