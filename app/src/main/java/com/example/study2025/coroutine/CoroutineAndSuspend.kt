package com.example.study2025.coroutine

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// launch
suspend fun getData() {
    CoroutineScope(Dispatchers.IO).launch {
        fetchUserProfile()
        fetchPosts()
    }
}


// async + await

data class User(val name:String, val age: Int)

suspend fun fetchUserProfile(): User {
    delay(500)
    return User("sashaa", 29)
}

suspend fun fetchPosts(): List<String> {
    delay(500)
    return listOf("post1", "post2", "post3")
}

fun main() = runBlocking {
    val userDeffered = async(Dispatchers.IO) {
        fetchUserProfile()
    }
    val postsDeferred = async(Dispatchers.IO) {
        fetchPosts()
    }

    val user = userDeffered.await()
    val posts = postsDeferred.await()

    println("User: $user")
    println("Posts: $posts")

    getData()


}
