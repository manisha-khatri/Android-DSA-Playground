package com.example.study2025.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    private val _name =  MutableLiveData<String>()
    val name: LiveData<String> = _name

    fun updateName(str: String) {
        _name.value = str;
    }
}