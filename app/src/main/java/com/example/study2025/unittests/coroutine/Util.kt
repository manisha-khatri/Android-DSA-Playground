package com.example.study2025.unittests.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Util(val dispatcher: CoroutineDispatcher) {
    suspend fun getUserName(): String {
        delay(500)
        return "CheezyCode"
    }

    suspend fun getUser(): String {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
        }
        return "User - CheezyCode"
    }

    suspend fun getAddress():String {
        withContext(dispatcher) {
            delay(2000)
        }
        return "Address"
    }

    var globalArg = false
    fun getAddressDetail() {
        CoroutineScope(dispatcher).launch {
            globalArg = true
        }
    }
}