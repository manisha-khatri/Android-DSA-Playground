package com.example.study2025.interviewPrep.shopsphere.presentation

import com.example.study2025.interviewPrep.shopsphere.domain.SearchSuggestion

data class SearchUiState(
    val query: String,
    val suggestions: List<SearchSuggestion>,
    val isLoading: Boolean,
    val error: String
)