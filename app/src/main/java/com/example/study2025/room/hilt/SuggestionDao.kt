package com.example.study2025.room.hilt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SuggestionDao {

    @Query("SELECT * FROM suggestions WHERE searchQuery=:query ORDER BY timeStamp DESC LIMIT 7")
    suspend fun getSuggestions(query: String): List<SuggestionEntity>

    @Query("DELETE FROM suggestions WHERE searchQuery=:query")
    suspend fun deleteSuggestion(query: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestions: List<SuggestionEntity>);
}