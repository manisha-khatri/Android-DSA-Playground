package com.example.study2025.mvvm.data.repository

import com.example.study2025.mvvm.data.model.User

class UserRepository {
    fun getUsers(): List<User> {
        return listOf(
            User("Alice", "alice@example.com"),
            User("Bob", "bob@example.com")
        )
    }
}
