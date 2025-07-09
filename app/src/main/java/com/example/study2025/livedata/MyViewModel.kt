package com.example.study2025.livedata

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.study2025.R

class MyViewModel: ViewModel() {
    private var _name = MutableLiveData<String>()
    var name: LiveData<String> = _name

    fun updateName(str: String) {
        _name.value = str
    }
}

class MyActivity: AppCompatActivity() {
    lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel.name.observe(this, Observer { updatedName ->
            Log.d("Observing change in name = ", updatedName)
        })

        val tv = findViewById<Button>(R.id.btn)
        tv.setOnClickListener {
            viewModel.updateName("hello");
        }
    }
}