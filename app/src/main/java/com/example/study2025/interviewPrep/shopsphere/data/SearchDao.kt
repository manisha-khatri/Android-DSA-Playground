package com.example.study2025.interviewPrep.shopsphere.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchDao {

    @Query("SELECT * FROM search_suggestions where `query`=:query ORDER BY timestamp DESC LIMIT 7")
    suspend fun getSuggestions(query: String): List<SearchSuggestionEntity>

    @Query("DELETE FROM search_suggestions where `query`=:query")
    suspend fun deleteSuggestion(query: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestions: List<SearchSuggestionEntity>)
}