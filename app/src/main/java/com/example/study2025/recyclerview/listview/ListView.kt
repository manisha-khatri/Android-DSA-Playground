package com.example.study2025.recyclerview.listview

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.R

class ListViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listview)

        val listView = findViewById<ListView>(R.id.listview)

        val names = listOf("Manisha", "elon", "Nikhil kamath")
        val adapter = ArrayAdapter(this, R.layout.item_name, names)

        listView.adapter = adapter;
    }
}