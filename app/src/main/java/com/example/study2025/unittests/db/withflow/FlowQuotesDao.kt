package com.example.study2025.unittests.db.withflow

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowQuotesDao {

    @Insert
    suspend fun insertQuote(quote: Quote)

    @Update
    suspend fun updateQuote(quote: Quote)

    @Query("DELETE FROM quote")
    suspend fun delete()

    @Query("SELECT * FROM quote")
    fun getQuotes(): Flow<List<Quote>>

    @Query("SELECT * FROM quote WHERE id = :quoteId")
    suspend fun getQuoteById(quoteId: Int): Quote

}
