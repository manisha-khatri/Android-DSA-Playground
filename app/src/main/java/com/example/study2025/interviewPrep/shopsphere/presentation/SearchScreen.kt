package com.example.study2025.interviewPrep.shopsphere.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.query

@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = { viewModel.onEvent(SearchEvent.QueryChanged(it)) },
            label = { Text(text = "Search Products")},
            modifier = Modifier.fillMaxWidth()
        )

        if(state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(8.dp))
        }

        if(state.suggestions.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(state.suggestions) { item ->
                    Text(
                        text = item.suggestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{ viewModel.onEvent(SearchEvent.SuggestionClicked(item.suggestion)) }
                            .padding(12.dp)
                    )
                }
            }
        } else if(!state.isLoading && state.query.isNotBlank()) {
            Text(
                text = "No suggestions found",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

    }
}






