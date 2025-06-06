package com.example.study2025.livedata2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.study2025.R
import java.util.Timer
import java.util.TimerTask

class TimerLiveData : LiveData<Long>() {

    private var timer: Timer? = null
    private var count = 0L

    override fun onActive() {
        super.onActive()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                postValue(count++)
            }
        }, 0, 1000) // every 1 second
    }

    override fun onInactive() {
        super.onInactive()
        timer?.cancel()
    }
}


class TimerViewModel : ViewModel() {
    val timer = TimerLiveData()
}

class TimerActivity: AppCompatActivity() {
    lateinit var timerViewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        timerViewModel.timer.observe(this, Observer { time -> //your timer starts automatically as soon as it is observed.
            Log.d("Timer", "updated = " + time)
        })

        // If you want the button to start or stop the timer explicitly, you'll need to add methods like start()
        // and stop() inside your TimerLiveData
    }
}