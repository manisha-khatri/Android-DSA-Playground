package com.example.study2025.interviewPrep.shopsphere.domain

import androidx.room.Query
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend operator fun invoke(query: String): List<SearchSuggestion> {
        return searchRepository.getSearchSuggestions(query)
    }
}