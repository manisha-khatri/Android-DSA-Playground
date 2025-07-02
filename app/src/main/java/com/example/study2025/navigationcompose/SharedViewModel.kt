package com.example.study2025.navigationcompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    fun setUserName(name: String) {
        _username.postValue(name)
    }
}