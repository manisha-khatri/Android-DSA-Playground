package com.example.study2025.unittests

import org.junit.Assert.*
import org.junit.Test

class PasswordValidatorTest {

    private val validator = Utils()

    @Test
    fun `empty password returns false`() {
        assertFalse(validator.isValid(""))
    }

    @Test
    fun `null password returns false`() {
        assertFalse(validator.isValid(null))
    }

    @Test
    fun `password shorter than 6 returns false`() {
        assertFalse(validator.isValid("abc"))
    }

    @Test
    fun `password longer than 15 returns false`() {
        assertFalse(validator.isValid("averylonggggpasswordexceedinglimit"))
    }

    @Test
    fun `password within valid range returns true`() {
        assertTrue(validator.isValid("abcdefg"))
    }
}