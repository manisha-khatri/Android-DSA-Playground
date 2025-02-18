package com.example.study2025.mvvm.data

class UserRepository {
    fun getUsers(): List<User> {
        return listOf(
            User("Alice", "alice@example.com"),
            User("Bob", "bob@example.com")
        )
    }
}
