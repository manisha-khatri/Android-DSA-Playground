package com.example.study2025.retrofit

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("users")
    suspend fun getUsers():List<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @POST("users")
    suspend fun createUser(@Body user: User)

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(@Field("username") username: String, @Field("password") password: String)
}
