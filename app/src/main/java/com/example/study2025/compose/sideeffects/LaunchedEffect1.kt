package com.example.study2025.compose.sideeffects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.composeapplication.ui.theme.ComposeApplicationTheme

class LaunchedEffect1: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .systemBarsPadding()
                ) { innerPadding ->
                    ListComposable(
                        Modifier.padding(innerPadding)
                    )
                }
            }

        }
    }

    @Composable
    fun ListComposable(modifiers: Modifier) {
        var categories = remember { mutableStateOf(emptyList<String>()) }

        LaunchedEffect(key1 = Unit) {
            categories.value = fetchCategories()
        }

        LazyColumn(
            modifier = modifiers
        ) {
            items(categories.value) {
                Text(text = it)
            }
        }
    }

    fun fetchCategories(): List<String> {
        return listOf("one", "two", "three")
    }
}


