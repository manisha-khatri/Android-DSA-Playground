package com.example.study2025.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
        var count: MutableState<Int> = rememberSaveable { mutableStateOf(0) }

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(1f)
        ) {

            NotificationCounter(count.value, { count.value++ })
            MessageBar(count.value)
        }
    }

    @Composable
    fun NotificationCounter(value: Int, increment: () -> Unit) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text= "You have sent $value notifications")
            Button(
                onClick = {
                    increment()
                    Log.d("Tag", "Button Clicked")
            }) {
                Text(text = "Send Notification")
            }
        }
    }

    @Composable
    fun MessageBar(value: Int) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "",
                    Modifier.padding(8.dp)
                )
                Text(
                    text = "Message sent so far - $value"
                )
            }
        }
    }

}















