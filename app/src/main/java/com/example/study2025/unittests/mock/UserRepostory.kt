package com.example.study2025.unittests.mock

class UserRepository {

    val users = listOf<User>(
        User(1, "John", "john@gmail.com", "12345"),
        User(1, "Wein", "wein@gmail.com", "12345"),
        User(1, "Emily", "emily@gmail.com", "12345")
    )

    fun loginUser(email: String, password: String): LOGIN_STATUS {
        val users = users.filter {user -> user.email == email }
        return if(users.size == 1) {
            if(users[0].password == password) {
                LOGIN_STATUS.SUCCESS
            } else {
                LOGIN_STATUS.INVALID_PASSWORD
            }
        } else {
            LOGIN_STATUS.INVALID_USER
        }
    }
}