package com.example.study2025.livedata

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.study2025.R

class CounterViewModel: ViewModel() {
    var _counterLiveData = MutableLiveData<String>()
    val counterLiveData: LiveData<String> = _counterLiveData

    private var counter = 0
    var job:Job? = null

    fun startCounter() {
        job = viewModelScope.launch(Dispatchers.IO) {
            for(i in 1..10) {
                delay(1000)
                counter++
                _counterLiveData.postValue("Counter: $counter")
            }
        }
    }

    fun stopCounter() {
        job?.cancel()
        _counterLiveData.postValue("Stopped")
    }
}

class CounterVMActivity : AppCompatActivity() {

    private lateinit var viewModel: CounterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv)

        viewModel = ViewModelProvider(this)[CounterViewModel::class.java]
        viewModel.counterLiveData.observe(this) { data ->
            tv.text = data
        }

        viewModel.startCounter()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopCounter()
    }
}
