package com.example.study2025.architecture.recipeapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study2025.architecture.recipeapp.domain.GetRecipesUseCase
import com.example.study2025.architecture.recipeapp.domain.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val getRecipesUseCase: GetRecipesUseCase) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    fun loadRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            _recipes.value = getRecipesUseCase()
        }
    }
}