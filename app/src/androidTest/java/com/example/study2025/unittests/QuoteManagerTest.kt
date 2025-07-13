package com.example.study2025.unittests

import androidx.test.core.app.ApplicationProvider
import android.content.Context
import com.google.gson.JsonSyntaxException
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.FileNotFoundException

class QuoteManagerTest {

    @Test(expected = FileNotFoundException::class)
    fun populateQuoteFromAsset_Test() {
        val quoteManager = QuoteManager()
        val context = ApplicationProvider.getApplicationContext<Context>()
        quoteManager.populateQuoteFromAsset(context, "")
    }

    @Test(expected = JsonSyntaxException::class)
    fun testPopulateQuoteFromAsset_invalidJSON_expected_Exception() {
        val quoteManager = QuoteManager()
        val context = ApplicationProvider.getApplicationContext<Context>()

        quoteManager.populateQuoteFromAsset(context, "malformed_quotes.json")
    }

    @Test
    fun testPopulateQuoteFromAsset_ValidJSON_expected_Count() {
        val quoteManager = QuoteManager()
        val context = ApplicationProvider.getApplicationContext<Context>()

        quoteManager.populateQuoteFromAsset(context, "quotes.json")
        assertEquals(15, quoteManager.quoteList.size)
    }

    @Test
    fun testPreviousQuote_expected_CorrectQuote() {
        val quoteManager = QuoteManager()
        quoteManager.populateQuotes(
           arrayOf(
               Quote("So many books, so little time.", "Frank Zappa"),
               Quote("A room without books is like a body without a soul.", "Marcus Tullius Cicero"),
               Quote("If you tell the truth, you don't have to remember anything.", "Mark Twain")
           )
        )

        val quote = quoteManager.getPreviousQuote()
        assertEquals("Frank Zappa", quote.author)
    }

    @Test
    fun testNextQuote_expected_CorrectQuote() {
        val quoteManager = QuoteManager()
        quoteManager.populateQuotes(
            arrayOf(
                Quote("So many books, so little time.", "Frank Zappa"),
                Quote("A room without books is like a body without a soul.", "Marcus Tullius Cicero"),
                Quote("If you tell the truth, you don't have to remember anything.", "Mark Twain")
            )
        )

        val quote = quoteManager.getNextQuote()
        assertEquals("Marcus Tullius Cicero", quote.author)
    }
}