package com.example.study2025.unittests

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StringReversalTest {
    private lateinit var stringReversal: Utils

    @Before
    fun setUp() {
        stringReversal = Utils()
    }

    @Test
    fun `reverse regular word`() {
        assertEquals("olleH", stringReversal.reverse("Hello"))
    }

    @Test
    fun `reverse empty string returns empty`() {
        assertEquals("", stringReversal.reverse(""))
    }

}