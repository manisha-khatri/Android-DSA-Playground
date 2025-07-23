package com.example.study2025.coroutine

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.study2025.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CoroutineScopesExample: AppCompatActivity() {
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById<TextView>(R.id.tv)

        lifecycleScope.launch(Dispatchers.Main) {
            tv.text = "hello world"

            /*println("Executing coroutineScope - CoroutineScope(Dispatchers.Main)")
            val withoutJob = CoroutineScopeWithoutJob()
            withoutJob.doWork()
*/
            println("Executing coroutineScope with Job - CoroutineScope(Dispatchers.Main + Job())")
            val withJob = CoroutineScopeWithJob2()
            withJob.doWork2()
        }

        GlobalScope.launch (Dispatchers.IO) { }
    }
}

class MyViewModel2: ViewModel() {
    fun update() {
        viewModelScope.launch{

        }
    }
}

// CoroutineScope
class CoroutineScopeWithoutJob {
    private val scope = CoroutineScope(Dispatchers.Main)

    fun doWork() {
        val childJob = GlobalScope.launch(Dispatchers.Main) {
            repeat(5) {
                println("GlobalScope child running: $it")
                delay(500)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            println("Cancelling parent scope...")
            scope.cancel()  // Cancels parent scope only
            println("Global child isActive after cancel: ${childJob.isActive}")
            println("Global child isActive after cancel: ${scope.isActive}")
        }
    }
}

class CoroutineScopeWithJob {
    private val parentJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + parentJob)

    fun doWork() {
        val childJob = GlobalScope.launch(Dispatchers.Main + parentJob) {
            repeat(5) {
                println("GlobalScope child running: $it")
                delay(500)
            }
        }

        println("Parent isActive before cancel: ${parentJob.isActive}")

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            println("Cancelling parent scope...")
            parentJob.cancel()  // Cancels parent scope only
            println("Parent isActive after cancel: ${parentJob.isActive}")
            println("Global child isActive after cancel: ${childJob.isActive}")
            println("Global child isActive after cancel: ${scope.isActive}")
        }
    }
}

class CoroutineScopeWithJob2 {
    private val parentJob = Job()

    fun doWork() {
        val scope1 = CoroutineScope(Dispatchers.Main + parentJob)
        val scope2 = GlobalScope.launch(Dispatchers.Main + parentJob) { }
        val scope3 = GlobalScope.launch(Dispatchers.Main + parentJob) { }

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            parentJob.cancel()  // Cancels parent scope only
            println(parentJob.isActive) //false
            println(scope1.isActive) //false
            println(scope2.isActive) //false
            println(scope3.isActive) //false
        }
    }

    fun doWork2() {
        CoroutineScope(Dispatchers.IO).launch {
            println("Added delay")
            //delay(5000)
            Thread.sleep(5000)
            repeat(4) {
                println("$it")
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            repeat(4) {
                println("Code outside the delay block - $it")
            }
        }
    }


}

