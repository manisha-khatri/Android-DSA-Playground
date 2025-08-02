package com.example.study2025.room.hilt

import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: SuggestionDao) {

    suspend fun saveSuggestion(query: String, suggestions: List<String>) {
        dao.deleteSuggestion(query)

        val entities = suggestions.map {
            SuggestionEntity(searchQuery = query, name = it)
        }
        dao.insertSuggestion(entities)
    }

    suspend fun getSuggestions(query: String): List<SuggestionEntity> {
        return dao.getSuggestions(query)
    }
}