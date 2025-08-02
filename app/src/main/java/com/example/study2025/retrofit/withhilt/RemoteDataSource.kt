package com.example.study2025.retrofit.withhilt

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: ApiService) {

    suspend fun getUsersByLastName(lastName: String): List<User> {
        return try {
            api.getUserByLastName(lastName)
        } catch(e: Exception) {
            emptyList()
        }
    }
}