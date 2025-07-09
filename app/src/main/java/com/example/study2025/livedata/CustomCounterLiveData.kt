package com.example.study2025.livedata

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.example.study2025.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CounterLiveData : LiveData<String>() {
    private var counter = 0

    override fun onActive() {
        super.onActive()
        postValue("Started")
    }

    override fun onInactive() {
        super.onInactive()
        postValue("Not Active")
    }

    fun updateData() {
        counter++
        postValue("Counter: $counter")
    }
}

class LiveCounterActivity : AppCompatActivity() {

    private val counterData = CounterLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv)

        counterData.observe(this) { data ->
            tv.text = data
        }

        // Use lifecycleScope to ensure coroutine is lifecycle-aware
        lifecycleScope.launch(Dispatchers.IO) {
            for (i in 1..20) {
                delay(1000)
                counterData.updateData()
            }
        }
    }
}
