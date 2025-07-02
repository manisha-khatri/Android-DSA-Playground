package com.example.study2025.navigationcompose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.study2025.navigationxml.SharedViewModel

@Composable
fun ScreenA(onBtnClick: (String) -> Unit) {
    var state by remember { mutableStateOf("") }

    Column{
        Text(
            text = "Screen A",
            modifier = Modifier.padding(8.dp)
        )
        Spacer(Modifier.height(20.dp))
        TextField(
            value = state,
            onValueChange = { state = it },
            modifier = Modifier.padding(8.dp)
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { onBtnClick(state) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Go To Screen B")
        }
    }
}