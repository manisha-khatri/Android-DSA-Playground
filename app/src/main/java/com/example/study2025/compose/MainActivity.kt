package com.example.study2025.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apollographql.apollo.api.label
import com.example.study2025.R



class MainActivity20: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColumnLayout()
        }
    }

    @Composable
    fun HelloWorld(name: String) {
        Text(text= "Hello $name")
    }

    //@Preview(showBackground = true, name = "Message 1", showSystemUi = true, widthDp = 200, heightDp = 200)
    @Composable
    private fun PreviewHelloWorld() {
        HelloWorld(name = "Manisha")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomText() {
    Text(
        text = "Hello World!!",
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Red,
        fontSize = 36.sp,
        textAlign = TextAlign.Right
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomImage() {
    Image(
        painter = painterResource(id = R.drawable.heart2),
        contentDescription = "dummy Image",
        colorFilter = ColorFilter.tint(Color.Blue),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            containerColor = Color.Gray
        ),
        enabled = true
    ) {
        Text(text = "Hello")
        Image(painter = painterResource(R.drawable.apple), contentDescription = "apple image")
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun CustomTextField() {
    TextField(
        value = "Hello Manisha",
        onValueChange = {},
        label = { Text(text = "Enter Message")},
        placeholder = {}
    )
}

@Composable
fun demo(){
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

    }
}

@Composable
private fun ReactiveTextInput() {
    val state = remember { mutableStateOf("") }
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        label = { Text(text = "Enter Message")},
        placeholder = {}
    )
}

/**
 * Column --> Linear Layout (Arrange Vertically)
 * Row --> Arrange Horizontally
 */
@Preview(showBackground = true, widthDp = 300, heightDp = 500)
@Composable
private fun ColumnLayout() {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "A", fontSize = 24.sp)
        Text(text = "B", fontSize = 24.sp)
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 500)
@Composable
private fun RowColumn() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "C", fontSize = 24.sp)
        Text(text = "D", fontSize = 24.sp)
    }
}

/**
 * Box --> Frame Layout
 */
@Preview(showBackground = true, widthDp = 300, heightDp = 500)
@Composable
private fun BoxLayout() {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "M", fontSize = 24.sp)
        Text(text = "N", fontSize = 24.sp)
    }
}



@Preview(showBackground = true)
@Composable
fun ListViewItem() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "image",
            Modifier.size(140.dp)
        )
        Column{
            Text(text = "John Doe", fontWeight = FontWeight.ExtraBold)
            Text(
                text = "Software Developer",
                fontWeight = FontWeight.Thin,
                fontSize = 12.sp
            )
        }
    }
}













