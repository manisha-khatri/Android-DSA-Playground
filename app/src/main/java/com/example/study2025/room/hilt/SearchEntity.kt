package com.example.study2025.room.hilt

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suggestions")
data class SuggestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val searchQuery: String,
    val timeStamp: Long = System.currentTimeMillis()
)