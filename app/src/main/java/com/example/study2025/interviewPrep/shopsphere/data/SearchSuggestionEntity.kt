package com.example.study2025.interviewPrep.shopsphere.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_suggestions")
data class SearchSuggestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val query: String,
    val suggestion: String,
    val timestamp: Long = System.currentTimeMillis()
)