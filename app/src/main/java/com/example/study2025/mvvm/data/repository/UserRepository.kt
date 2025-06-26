package com.example.study2025.mvvm.data.repository

import com.example.study2025.mvvm.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    fun getUsers(): List<User> {
        return listOf(
            User("Alice", "alice@example.com"),
            User("Bob", "bob@example.com")
        )
    }

    suspend fun getSortedUsers(): List<User> {
        return withContext(Dispatchers.Default) {
            getUsers().sortedBy { it.name } // Sorting is CPU-intensive
        }
    }

}
