package com.example.study2025.workmanager

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun fetchAndLogData() {
        try {
            val response = apiService.fetchData()
            if (response.isSuccessful) {
                Log.d("DataRepository", "Data fetched: ${response.body()}")
            } else {
                Log.e("DataRepository", "Failed: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("DataRepository", "Error: ${e.localizedMessage}")
        }
    }
}