package com.example.study2025.unittests

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runners.Parameterized
import org.junit.runner.RunWith

@RunWith(value = Parameterized::class)
class ParameterizedExample(val input: String, val expectedValue :Boolean) {

    @Test
    fun test() {
        val utils = Utils()
        val result = utils.isPalindrome(input)
        assertEquals(expectedValue, result)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index} : {0} is palindrome - {1}")
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf("hello", false),
                arrayOf("level", true),
                arrayOf("a", true),
                arrayOf("", true)
            )
        }
    }
}