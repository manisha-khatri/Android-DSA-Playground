package com.example.study2025.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewModel: ViewModel() {
    var counter = MutableLiveData<Int>()

    fun increment() {
        val current = counter.value ?: 0
        counter.value = current + 1
    }
}

class CounterActivity: AppCompatActivity() {
    lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        viewModel.counter.observe(this) { count ->
            //tv.text = count
        }

        //increment()
    }
}