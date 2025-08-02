package com.example.study2025.room.withouthilt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    suspend fun getAllUsersOnce(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }

    suspend fun insertUser(user: User) {
        userRepository.insertUser(user)
    }

    suspend fun getUser(userId: Int): User {
        return userRepository.getUserById(userId)
    }

    suspend fun updateUser(user: User) {
        userRepository.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userRepository.deleteUser(user)
    }
}
