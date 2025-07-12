package com.example.study2025.architecture.recipeapp.data

import com.example.study2025.architecture.recipeapp.domain.Recipe
import com.example.study2025.architecture.recipeapp.domain.RecipeRepository

class RecipeRepositoryImpl: RecipeRepository {

    override suspend fun getRecipes(): List<Recipe> {
        return listOf(
            Recipe(1, "Pasta", "Delicious creamy pasta."),
            Recipe(2, "Paneer Butter Masala", "Rich Indian curry.")
        )
    }
}