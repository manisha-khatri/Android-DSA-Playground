package com.example.study2025.unittests.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quote::class], version =1)
abstract class QuoteDatabase: RoomDatabase() {

    abstract fun quoteDao(): QuotesDao

    // No need to write test for DB, As its a framework which has already been tested by google
}