package com.example.study2025.unittests.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuotesDaoTest {

    // Execute the code synchronously of all the architecture components
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var quoteDatabase: QuoteDatabase
    lateinit var quotesDao: QuotesDao

    @Before
    fun setUp() {
        /**
         * For each testcase setting up a in memory database
         * Not an actual database, live till the application is running
         */
        quoteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            QuoteDatabase::class.java
        ).allowMainThreadQueries().build()

        quotesDao = quoteDatabase.quoteDao()
    }

    @Test
    fun insertQuote_expectedSingleQuote() = runBlocking { // Hold the thread till all the suspend execution completes
        val quote = Quote(0, "This is a test Quote", "CheezyCode")
        quotesDao.insertQuote(quote)

        val result = quotesDao.getQuotes().getOrAwaitValue() // observing LiveData value

        assertEquals(1, result.size)
        assertEquals("This is a test Quote", result[0].text)
    }

    @Test
    fun deleteQuote_expectedNoResult() = runBlocking {
        val quote = Quote(0, "This is a test quote", "CheezyCode")
        quotesDao.insertQuote(quote)

        quotesDao.delete()

        val result = quotesDao.getQuotes().getOrAwaitValue()

        assertEquals(0, result.size)
    }

    @After
    fun tearDown() {
        quoteDatabase.close()
    }
}