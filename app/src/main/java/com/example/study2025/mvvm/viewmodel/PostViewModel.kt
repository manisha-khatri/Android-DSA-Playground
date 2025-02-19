package com.example.study2025.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.study2025.mvvm.data.model.Post
import com.example.study2025.mvvm.data.repository.PostRepository

class PostViewModel: ViewModel() {
    private val postRepository = PostRepository()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    fun fetchPosts() {
        postRepository.fetchPosts(_posts)
    }
}