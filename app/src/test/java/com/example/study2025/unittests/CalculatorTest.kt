package com.example.study2025.unittests

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorTest {
    private lateinit var calculator: Calculator

    @Before
    fun setup(){
        calculator = Calculator()
    }

    @Test
    fun `addition should return correct sum`() {
        val result = calculator.add(1,3)
        assertEquals(4, result)
    }
}