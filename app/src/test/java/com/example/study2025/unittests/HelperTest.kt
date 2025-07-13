package com.example.study2025.unittests

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HelperTest {

    lateinit var utils: Utils

    @Before
    fun setUp() {
        utils = Utils()
    }

    @After
    fun tearDown() {
        println("After Every Test Case")
    }

    @Test
    fun isPalindrome() {
        val result = utils.isPalindrome("hello")

        assertEquals(false, result)
    }

    @Test
    fun isPalindrome_inputString_level_expected_true() {
        val result = utils.isPalindrome("level")

        assertEquals(true, result)
    }

}