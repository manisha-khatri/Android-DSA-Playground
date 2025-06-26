package com.example.study2025.callbackinterface

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

interface UserCallback {
    fun onSuccess(name: String)
    fun onFailure(error: String)
}

class UserRepository {
    fun fetchUser(callback: UserCallback) {
        Thread {
            try {
                Thread.sleep(1000);
                val success = true

                if(success) {
                    callback.onSuccess("Manisha")
                } else {
                    callback.onFailure("Error")
                }
            } catch (e:Exception) {
                callback.onFailure("Error " + e)
            }
        }.start()
    }
}

class UserActivity: AppCompatActivity() {
    private val repository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository.fetchUser(object: UserCallback{
            override fun onSuccess(name: String) {
                // update UI
            }

            override fun onFailure(error: String) {
                //show error
            }
        })


    }
}