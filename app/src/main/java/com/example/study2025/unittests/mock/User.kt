package com.example.study2025.unittests.mock

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)

enum class LOGIN_STATUS {
    INVALID_USER,
    INVALID_PASSWORD,
    UNKNOWN_ERROR,
    SUCCESS
}