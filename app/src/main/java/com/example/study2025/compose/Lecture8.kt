package com.example.study2025.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

class Lecture8: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationScreen()
        }
    }

    @Preview(showBackground = true, showSystemUi = true, widthDp = 300, heightDp = 300)
    @Composable
    fun NotificationScreen() {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            NotificationCounter()
        }
    }

    @Composable
    fun NotificationCounter() {
        var count: MutableState<Int> = remember { mutableStateOf(0) }

        Column(verticalArrangement = Arrangement.Center) {
            Text(text= "You have sent ${count.value} notifications")
            Button(onClick = {
                count.value ++
                Log.d("Tag", "Button Clicked")
            }) {
                Text(text = "Send Notification")
            }
        }
    }


































}















