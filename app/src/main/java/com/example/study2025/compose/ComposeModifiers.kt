package com.example.study2025.compose

import android.icu.number.Scale
import com.example.study2025.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.nio.file.WatchEvent

class ComposeModifiers: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CircularImage()
        }
    }

    @Preview
    @Composable
    private fun TextComposable() {
        Text(
            text = "Hello World",
            color = Color.White,
            modifier = Modifier.background(Color.Blue)
                .size(200.dp)
                .padding(20.dp)
                .border(4.dp, Color.Red)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { }
        )
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun CircularImage() {
        Image(
            painter = painterResource(R.drawable.manisha),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(80.dp)
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape)

        )
    }
}