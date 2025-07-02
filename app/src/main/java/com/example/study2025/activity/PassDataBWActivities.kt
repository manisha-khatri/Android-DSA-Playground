package com.example.study2025.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.parcel.Parcelize

class A: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //on click
        val intent = Intent(this, B::class.java)
        intent.putExtra("name", "manisha")
        intent.putExtra("age", 19)

        val user = User("manisha", 29)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}

class B: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age", 0)

        val user = intent.getParcelableExtra<User>("user")
    }
}

//🏃‍♀️ Passing Parcelable Object
@Parcelize
data class User(val name: String, val age: Int): Parcelable