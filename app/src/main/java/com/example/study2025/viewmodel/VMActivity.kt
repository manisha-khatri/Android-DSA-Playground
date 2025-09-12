package com.example.study2025.viewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VMActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vmp = ViewModelProvider(this)
        val vm = vmp.get(MyViewModel::class.java)
    }
}

class MyViewModel(): ViewModel() {

}