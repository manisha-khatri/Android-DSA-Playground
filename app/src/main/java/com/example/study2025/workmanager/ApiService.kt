package com.example.study2025.workmanager


import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("posts/1")
    suspend fun fetchData(): Response<Any>
}