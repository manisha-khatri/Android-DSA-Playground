package com.example.study2025.mvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.study2025.databinding.ActivityUsersBinding
import com.example.study2025.mvvm.viewmodel.PostViewModel
import com.example.study2025.mvvm.viewmodel.UserViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel
    private lateinit var binding: ActivityUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        userViewModel.users.observe(this) { users ->
            binding.tvUsers.text = users.joinToString("\n") { it.name }
        }

        postViewModel.posts.observe(this) { posts ->
            binding.tvPosts.text = posts.joinToString("\n") {
                it.title + " " + it.userId
            }
        }

        binding.btnFetch.setOnClickListener {
            userViewModel.fetchUsers()
        }

        binding.btnFetchPosts.setOnClickListener {
            postViewModel.fetchPosts()
        }
    }
}
