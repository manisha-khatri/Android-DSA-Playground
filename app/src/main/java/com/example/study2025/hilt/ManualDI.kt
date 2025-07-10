package com.example.study2025.hilt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ApiService {
    fun fetchData(): String = "Fetch Data from API"
}

class Repository (val apiService: ApiService) {
    fun getData(): String = apiService.fetchData()
}

class MainViewModel(val repository: Repository) {
    fun loadData(): String = repository.getData()
}

class MainActivity13: AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = ApiService()
        val repository = Repository(apiService)

        viewModel = MainViewModel(repository)

        println(viewModel.loadData())
    }
}

/**
 * 🔍 Problems:
 *
 * You are responsible for creating and passing dependencies everywhere.
 *
 * Reuse and testing are hard.
 *
 * No clear lifecycle management.
 *
 * Scaling is a nightmare as the app grows.
 */