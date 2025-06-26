package com.example.study2025.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.study2025.R

class CustomComposable: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{

        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun CustomListViewPreview() {
        Column {
            CustomListView(R.drawable.ic_launcher_foreground, "Manisha", "Software Engineer", Modifier)
            CustomListView(R.drawable.heart2, "Manisha", "Software Engineer", Modifier)
            CustomListView(R.drawable.apple, "Manisha", "Software Engineer", Modifier)
            CustomListView(R.drawable.heart_image, "Manisha", "Software Engineer", Modifier)
            CustomListView(R.drawable.ic_launcher_foreground, "Manisha", "Software Engineer", Modifier)
        }
    }

    @Composable
    fun CustomListView(resId: Int, name: String, occupation: String, modifier: Modifier) {
        Row(
            modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(resId),
                contentDescription = "Image",
                Modifier.size(22.dp)
            )
            Column {
                Text(text = name, fontWeight = FontWeight.ExtraBold)
                Text(text = occupation, fontWeight = FontWeight.Thin, fontSize = 12.sp)
            }
        }
    }
}