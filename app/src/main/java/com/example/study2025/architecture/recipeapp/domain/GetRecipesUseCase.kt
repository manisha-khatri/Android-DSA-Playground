package com.example.study2025.architecture.recipeapp.domain

import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke() = repository.getRecipes()
}