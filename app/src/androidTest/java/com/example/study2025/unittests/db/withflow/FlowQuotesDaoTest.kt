package com.example.study2025.unittests.db.withflow

import app.cash.turbine.test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.study2025.unittests.db.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class FlowQuotesDaoTest {

    // Execute the code synchronously of all the architecture components
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var quoteDatabase: FlowQuoteDatabase
    lateinit var quotesDao: FlowQuotesDao

    @Before
    fun setUp() {
        /**
         * For each testcase setting up a in memory database
         * Not an actual database, live till the application is running
         */
        quoteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FlowQuoteDatabase::class.java
        ).allowMainThreadQueries().build()

        quotesDao = quoteDatabase.quoteDao()
    }

    @Test
    fun insertQuote_expectedSingleQuote() = runBlocking { // Hold the thread till all the suspend execution completes
        val quote = Quote(0, "This is a test Quote", "CheezyCode")
        quotesDao.insertQuote(quote)

        val result = quotesDao.getQuotes().first()

        assertEquals(1, result.size)
        assertEquals("This is a test Quote", result[0].text)
    }

    @Test
    fun insertQuote_expectedSingleQuoteWithToListTurbine() = runBlocking { // Hold the thread till all the suspend execution completes
        val quote = Quote(0, "This is a test Quote", "CheezyCode")
        val quote2 = Quote(0, "This is a test Quote 2", "CheezyCode")
        quotesDao.insertQuote(quote)
        quotesDao.insertQuote(quote2)

        /**
         * Will run infinitely, because in the case of Room database flow keep on giving data updates
         * thats why flow won't complete
         */
        //val result = quotesDao.getQuotes().toList()

        val result = quotesDao.getQuotes().test {
            val quoteList = awaitItem()
            assertEquals(2, quoteList.size)
            cancel()
        }
    }

    @Test
    fun insertQuote_expectedSingleQuote_AddedDelay_SimulatingUserAction() = runBlocking { // Hold the thread till all the suspend execution completes
        val quote = Quote(0, "This is a test Quote", "CheezyCode")
        val quote2 = Quote(0, "This is a test Quote 2", "CheezyCode")

        quotesDao.insertQuote(quote)
        launch {
            delay(500)
            quotesDao.insertQuote(quote2)
        }

        val result = quotesDao.getQuotes().test {
            val quoteList = awaitItem()
            assertEquals(1, quoteList.size)

            val quoteList2 = awaitItem()
            assertEquals(2, quoteList2.size)

            cancel()
        }
    }


    @After
    fun tearDown() {
        quoteDatabase.close()
    }
}