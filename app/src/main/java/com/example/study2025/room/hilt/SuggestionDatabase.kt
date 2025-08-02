package com.example.study2025.room.hilt

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SuggestionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SuggestionDatabase: RoomDatabase() {
    abstract fun suggestionDao(): SuggestionDao
}