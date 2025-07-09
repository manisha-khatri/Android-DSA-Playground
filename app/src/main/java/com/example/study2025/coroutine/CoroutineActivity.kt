package com.example.study2025.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the when all the users collected -- not like flow
        CoroutineScope(Dispatchers.Main).launch {
            getUsers().forEach { name ->
                println("user - $name")
            }
        }
    }
}


suspend fun getUsers(): List<String> {
    val list = mutableListOf<String>()
    list.add(getUser(1))
    list.add(getUser(2))
    list.add(getUser(3))
    list.add(getUser(4))

    return list
}

suspend fun getUser(index: Int): String {
    delay(1000)
    return "Person $index"
}
