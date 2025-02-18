package com.example.study2025.mvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.study2025.databinding.ActivityUsersBinding
import com.example.study2025.mvvm.viewmodel.UserViewModel

class UserActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        userViewModel.users.observe(this) { users ->
            binding.tvUsers.text = users.joinToString("\n") { it.name }
        }

        binding.btnFetch.setOnClickListener {
            userViewModel.fetchUsers()
        }
    }
}
