package com.example.study2025.mvvm.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.study2025.mvvm.data.api.ApiService
import com.example.study2025.mvvm.data.model.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostRepository {
    private val apiService: ApiService

    init {
        val retrofit =  Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")  // Base URL for JSONPlaceholder
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    /*fun fetchPosts(posts: MutableLiveData<List<Post>>) {
        apiService.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if(response.isSuccessful) {
                    posts.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                posts.value = emptyList()
            }
        })
    }*/

    suspend fun fetchPosts(): List<Post> {
        return try {
            apiService.getPosts() // Retrofit suspend function
        } catch (e: Exception) {
            emptyList() // Return empty list on failure
        }
    }
}