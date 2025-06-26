package com.example.study2025.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study2025.mvvm.data.model.Post
import com.example.study2025.mvvm.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel: ViewModel() {
    private val postRepository = PostRepository()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

/*    fun fetchPosts1() {
        postRepository.fetchPosts(_posts)
    }*/

    fun fetchPosts() {
        viewModelScope.launch {
            _posts.value = withContext(Dispatchers.IO) {
                postRepository.fetchPosts();
            }
        }
    }

}