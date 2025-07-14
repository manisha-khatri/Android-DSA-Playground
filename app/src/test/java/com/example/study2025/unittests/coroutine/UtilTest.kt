package com.example.study2025.unittests.coroutine

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class UtilTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Test
    fun testGetUserName() {
        val sut = Util(mainCoroutineRule.testDispatcher)

        runTest {
            sut.getUserName()
        }
    }

    @Test
    fun testIODispatcher() {
        val sut = Util(mainCoroutineRule.testDispatcher)

        runTest {
            sut.getAddress()
        }
    }

    // As mainCoroutineRule schedule all the coroutines on a single thread
    // checking
    @Test
    fun testGetAddressDetail() {
        val sut = Util(mainCoroutineRule.testDispatcher)
        runTest {
            sut.getAddressDetail()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(true, sut.globalArg)
        }
    }


}