package com.example.study2025.recyclerview.viewtypes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.study2025.R

class MainActivity1: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val items = listOf(
            ListItem.Header,
            ListItem.TextItem("Manisha Khatri"),
            ListItem.ImageItem(R.drawable.ic_launcher_background),
            ListItem.TextItem("Elon Musk"),
            ListItem.ImageItem(R.drawable.ic_launcher_foreground)
        )

        val adapter = MultiViewAdapter(items)
        recyclerView.adapter = adapter

    }
}