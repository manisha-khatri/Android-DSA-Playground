package com.example.study2025.compose.quoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.study2025.compose.quoteapp.screens.QuoteDetail
import com.example.study2025.compose.quoteapp.screens.QuoteListScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity6: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            DataManager.loadAssetsFromFile(applicationContext)
        }
        setContent {
            Scaffold { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    App()
                }
            }
        }
    }

    @Composable
    fun App() {
        if(DataManager.isDataLoaded.value) {
            if(DataManager.currentPage.value == Pages.LISTING) {
                QuoteListScreen(data = DataManager.data) { quote ->
                    DataManager.switchPages(quote)
                }
            } else {
                DataManager.currentQuote?.let {
                    QuoteDetail(quote = it)
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(1f)
            ) {
                Text(
                    text = "Loading....",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

enum class Pages {
    LISTING,
    DETAIL,
}