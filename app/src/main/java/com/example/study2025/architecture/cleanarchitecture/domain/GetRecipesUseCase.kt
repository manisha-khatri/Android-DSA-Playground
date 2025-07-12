package com.example.study2025.architecture.cleanarchitecture.domain

class GetRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke() = repository.getRecipes()
}