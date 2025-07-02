package com.example.study2025.navigationcompose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScreenB(viewModel: SharedViewModel = hiltViewModel()) {
    val name by viewModel.username.observeAsState("")

    Column{
        Text(
            text = "Welcome to Screen B",
            modifier = Modifier.padding(8.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Username: $name",
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
    }
}