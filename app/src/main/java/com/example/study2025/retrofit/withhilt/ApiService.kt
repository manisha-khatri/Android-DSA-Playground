package com.example.study2025.retrofit.withhilt

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUserList(): List<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): User

    @GET("users/list")
    suspend fun getUserByLastName(@Query("lastName") lastName: String): List<User>

    @POST("users")
    suspend fun insertUser(@Body user: User)
}