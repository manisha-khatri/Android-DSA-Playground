package com.example.study2025.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.study2025.mvvm.data.User
import com.example.study2025.mvvm.data.UserRepository

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun fetchUsers() {
        _users.value = repository.getUsers()
    }
}
