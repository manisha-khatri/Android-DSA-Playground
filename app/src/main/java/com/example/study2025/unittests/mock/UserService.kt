package com.example.study2025.unittests.mock

import com.example.study2025.unittests.mock.LOGIN_STATUS.*

class UserService(private val userRepository: UserRepository) {

    fun loginUser(email:String, password: String): String {
        val status = userRepository.loginUser(email, password)
        return when(status) {
            INVALID_USER -> "User does not exist!"
            INVALID_PASSWORD -> "Password invalid!"
            UNKNOWN_ERROR -> "Unknown error occured!"
            SUCCESS -> "Logged in successfully!"
        }
    }
}