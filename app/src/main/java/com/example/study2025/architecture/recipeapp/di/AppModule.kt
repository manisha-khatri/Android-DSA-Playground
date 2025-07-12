package com.example.study2025.architecture.recipeapp.di

import com.example.study2025.architecture.recipeapp.data.RecipeRepositoryImpl
import com.example.study2025.architecture.recipeapp.domain.GetRecipesUseCase
import com.example.study2025.architecture.recipeapp.domain.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRecipeRepository(): RecipeRepository = RecipeRepositoryImpl()

    @Provides
    fun provideGetRecipesUseCase(repository: RecipeRepository): GetRecipesUseCase = GetRecipesUseCase(repository)

}