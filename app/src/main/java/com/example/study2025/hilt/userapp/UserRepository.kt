package com.example.study2025.hilt.userapp

import android.util.Log
import javax.inject.Inject

const val TAG = "HILT_TAG"

interface UserRepository {
    fun saveUser(email: String, password: String)
}

class SQLRepository @Inject constructor(val loggerService: LoggerService): UserRepository { //Constructor injection
    override fun saveUser(email: String, password: String) {
        Log.d(TAG, "User saved in SQL DB")
    }
}

class FirebaseRepository: UserRepository {
    override fun saveUser(email: String, password: String) {
        Log.d(TAG, "User saved in Firebase")
    }

}

