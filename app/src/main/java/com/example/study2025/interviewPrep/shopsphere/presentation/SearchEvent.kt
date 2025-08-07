package com.example.study2025.interviewPrep.shopsphere.presentation

sealed class SearchEvent {
    data class QueryChanged(val query: String): SearchEvent()
    data class SuggestionClicked(val suggestion: String): SearchEvent()
    object OnRetrySearch: SearchEvent()
    object ClearQuery: SearchEvent()
}