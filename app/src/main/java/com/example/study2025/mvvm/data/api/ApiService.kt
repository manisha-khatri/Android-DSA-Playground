package com.example.study2025.mvvm.data.api

import com.example.study2025.mvvm.data.model.Post
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("posts") // Endpoint that returns posts data
    //fun getPosts(): Call<List<Post>>  // Returns a list of posts
    suspend fun getPosts(): List<Post>  // Returns a list of posts
}