package com.example.study2025.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.example.study2025.R

class ComposeWithXML: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose_with_xml)
        val composeLayout = findViewById<ComposeView>(R.id.compose_layout)
        composeLayout.setContent {
            HelloWorld("Manisha")
        }
    }
}

@Composable
fun HelloWorld(name: String) {
    Text(text = "Hello $name")
}
