package com.example.study2025.adapters

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.R

class ListViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_activity_main)

        val listView: ListView = findViewById(R.id.listview)
        val data = listOf("Apple", "Banana", "Mango", "Orange")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, data)
        listView.adapter = adapter
    }
}

