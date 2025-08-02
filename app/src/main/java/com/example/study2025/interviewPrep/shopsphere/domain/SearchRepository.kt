package com.example.study2025.interviewPrep.shopsphere.domain

interface SearchRepository {
    suspend fun getSearchSuggestions(query: String): List<SearchSuggestion>
}