package com.example.study2025.architecture.cleanarchitecture.domain

interface RecipeRepository {
    suspend fun getRecipes(): List<Recipe>
}