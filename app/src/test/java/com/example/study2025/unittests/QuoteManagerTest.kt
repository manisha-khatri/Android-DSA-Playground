package com.example.study2025.unittests

import android.content.Context
import android.content.res.AssetManager
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn

class QuoteManagerTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var assetManager: AssetManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test() {
        val testStream = QuoteManagerTest::class.java.getResourceAsStream("/test_quotes.json")
        doReturn(assetManager).`when`(context).assets
        Mockito.`when`(context.assets.open(anyString())).thenReturn(testStream)

        val sut = QuoteManager()
        sut.populateQuoteFromAsset(context, "")
        val quote = sut.getCurrentQuote()
        assertEquals("Be yourself; everyone else is already taken.", quote.text)
    }
}