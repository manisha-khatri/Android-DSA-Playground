package com.example.study2025.interviewPrep.shopsphere

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchScreen2(viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = {
                viewModel.onEvent(SearchEvent.QueryChanged(it))
            }
        )

        if(state.isLoading) {
            LinearProgressIndicator()
        }
    }
}

sealed class SearchEvent2 {
    data class OnQueryChanges(val query: String)
    data class OnSuggestionClicked(val suggestion: String)
}

