package com.example.study2025.callbackinterface

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

interface UserCallback2 {
    fun onSuccess(str: String)
    fun onFailure()
}

class FetchUserTask(private val callback: UserCallback2) : AsyncTask<Void, Void, String?>() {
    override fun doInBackground(vararg params: Void?): String? {
        return try {
            Thread.sleep(1000)
            val success = true
            if (success) "Manisha 👩‍💻" else null
        } catch (e: Exception) {
            null
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(result == null) {
            callback.onFailure()
        } else {
            callback.onSuccess(result)
        }
    }
}

class UserActivity2: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FetchUserTask(object: UserCallback2{
            override fun onSuccess(str: String) {
                TODO("Not yet implemented")
            }
            override fun onFailure() {
                TODO("Not yet implemented")
            }
        })

    }
}

