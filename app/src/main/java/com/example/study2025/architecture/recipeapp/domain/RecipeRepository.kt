package com.example.study2025.architecture.recipeapp.domain

interface RecipeRepository {
    suspend fun getRecipes(): List<Recipe>
}