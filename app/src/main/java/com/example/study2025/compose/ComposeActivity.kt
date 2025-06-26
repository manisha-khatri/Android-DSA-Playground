package com.example.study2025.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.study2025.R

data class Profile(val name: String, val role: String, val isActive: Boolean)

class ComposeActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloWorld()
        }
    }

    @Preview
    @Composable
    fun HelloWorld(name: String = "Manisha") {
        Text(text = "Hello World $name" )
    }

    /**
     * isActive : true
     * imageURL : "http.."
     * "name" : "CheezyCode"
     * "role" : "Developer"
     */


    @Composable
    fun profile(obj: Profile) {
        Row {
            Image (
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo"
            )
            Column {
               Text(text = "Name : ${obj.name}")
               Text(text = "Name : ${obj.role}")
                if(obj.isActive) {
                    Row {
                        Text(text = "Active")
                        Checkbox(checked = true, onCheckedChange = {})
                    }
                }
            }
        }
    }
}