package com.example.study2025.architecture.recipeapp.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RecipeScreen(viewModel: RecipeViewModel) {
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
    }

    LazyColumn {
        items(recipes) { recipe ->
            Text(text = recipe.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = recipe.description)
        }
    }
}
