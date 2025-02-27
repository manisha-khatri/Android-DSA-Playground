package com.example.study2025.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    val data: MutableLiveData<String> = MutableLiveData()

}