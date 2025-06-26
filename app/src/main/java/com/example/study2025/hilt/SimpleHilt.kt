package com.example.study2025.hilt

import android.app.Application
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
@HiltAndroidApp
class MyApp: Application()
*/

class ApiService2 @Inject constructor() {
    fun fetchData(): String = "Fetch data from API"
}

class MyRepository @Inject constructor(val apiService: ApiService2) {
    fun getData(): String = apiService.fetchData()
}

@HiltViewModel
class MyViewModel @Inject constructor(val repository: MyRepository): ViewModel() {
    fun loadData(): String = repository.getData()
}

@AndroidEntryPoint
class MainActivity2: AppCompatActivity() {
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData()
    }
}