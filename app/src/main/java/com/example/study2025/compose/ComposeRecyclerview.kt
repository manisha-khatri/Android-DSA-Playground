package com.example.study2025.compose

import com.example.study2025.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items

class ComposeRecyclerview: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRecyclerView()
        }

    }

    data class Category(val resId: Int, val title: String, val subTitle: String)

    @Composable
    fun ComposeRecyclerView() {
        LazyColumn {
            items(getCategoryList()) { item: Category ->
                BlogCategory(
                    imageId = item.resId,
                    title = item.title,
                    subTitle = item.subTitle
                )
            }
        }
    }


    @Preview(heightDp = 300)
    @Composable
    private fun BlogCategoryPreview() {
        Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
            getCategoryList().map { item ->
                BlogCategory(item.resId, item.title, item.subTitle)
            }
        }
    }

    @Composable
    fun BlogCategory(imageId: Int, title: String, subTitle: String) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.background(Color.Black).padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(imageId),
                    contentDescription = "",
                    Modifier.size(48.dp)
                        .weight(2f)
                )
                ItemDescription(title, subTitle, Modifier.weight(8f))
            }
        }
    }

    //Reusable composable
    @Composable
    fun ItemDescription(title: String, subTitle: String, modifier: Modifier) {
        Column (
            modifier = modifier
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = subTitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Thin
            )
        }
    }



    fun getCategoryList() : List<Category> {
        val list = mutableListOf<Category>()
        list.add(Category(R.drawable.user, "Android", "Kotlin"))
        list.add(Category(R.drawable.user, "iOS", "Swift"))
        list.add(Category(R.drawable.user, "Web", "Java Script"))
        list.add(Category(R.drawable.user, "Backend", "Java"))
        list.add(Category(R.drawable.user, "Android", "Kotlin"))
        list.add(Category(R.drawable.user, "iOS", "Swift"))
        list.add(Category(R.drawable.user, "Web", "Java Script"))
        list.add(Category(R.drawable.user, "Backend", "Java"))
        list.add(Category(R.drawable.user, "DevOps", "CI/CD & Docker"))
        list.add(Category(R.drawable.user, "Cloud", "AWS & GCP"))
        list.add(Category(R.drawable.user, "Database", "SQL & NoSQL"))
        list.add(Category(R.drawable.user, "Security", "OWASP & Auth"))
        list.add(Category(R.drawable.user, "AI/ML", "TensorFlow & PyTorch"))
        list.add(Category(R.drawable.user, "Blockchain", "Ethereum & Smart Contracts"))
        list.add(Category(R.drawable.user, "Design", "UI/UX Principles"))
        list.add(Category(R.drawable.user, "Testing", "JUnit & Espresso"))
        list.add(Category(R.drawable.user, "System Design", "Scalability & Architecture"))
        list.add(Category(R.drawable.user, "Tools", "Git & GitHub"))
        list.add(Category(R.drawable.user, "Web", "Java Script"))
        list.add(Category(R.drawable.user, "Backend", "Java"))
        list.add(Category(R.drawable.user, "DevOps", "CI/CD & Docker"))
        list.add(Category(R.drawable.user, "Cloud", "AWS & GCP"))
        list.add(Category(R.drawable.user, "Database", "SQL & NoSQL"))
        list.add(Category(R.drawable.user, "Security", "OWASP & Auth"))
        list.add(Category(R.drawable.user, "AI/ML", "TensorFlow & PyTorch"))
        list.add(Category(R.drawable.user, "Blockchain", "Ethereum & Smart Contracts"))
        list.add(Category(R.drawable.user, "Design", "UI/UX Principles"))
        list.add(Category(R.drawable.user, "Testing", "JUnit & Espresso"))
        list.add(Category(R.drawable.user, "System Design", "Scalability & Architecture"))
        list.add(Category(R.drawable.user, "Tools", "Git & GitHub"))

        return list
    }
}