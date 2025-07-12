package com.example.study2025.architecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class Recipe(val id: Int, val name: String, val ingredients: List<String>)

interface RecipeRepository {
    suspend fun getRecipes(): List<Recipe>
    suspend fun deleteRecipe(id: Int): Boolean
    suspend fun validateInput(input: Recipe): Boolean
}

class RecipeRepositoryImpl @Inject constructor(): RecipeRepository {

    private val _fakeRecipes = mutableListOf(
        Recipe(1, "Pasta", listOf("Wheat", "Salt", "Olive Oil")),
        Recipe(2, "Salad", listOf("Lettuce", "Tomato", "Cucumber"))
    )

    override suspend fun getRecipes(): List<Recipe> {
        return _fakeRecipes.toList()
    }

    override suspend fun deleteRecipe(id: Int): Boolean {
        return _fakeRecipes.removeIf { it.id == id }
       /* val initialSize = _fakeRecipes.size
        _fakeRecipes.removeIf { it.id == id }
        return _fakeRecipes.size < initialSize*/
    }

    override suspend fun validateInput(input: Recipe): Boolean {
        return input.name.isNotBlank() && input.ingredients.isNotEmpty()
    }

}

class GetRecipesUseCase @Inject constructor(val repository: RecipeRepository) {
    suspend operator fun invoke(): List<Recipe> = repository.getRecipes()
}

class DeleteRecipeUseCase @Inject constructor(val repository: RecipeRepository) {
    suspend operator fun invoke(id: Int): Boolean = repository.deleteRecipe(id)
}

class ValidateRecipeInputUseCase @Inject constructor(val repository: RecipeRepository) {
    suspend operator fun invoke(recipe: Recipe) = repository.validateInput(recipe)
}

data class RecipeUseCases @Inject constructor(
    val getRecipes: GetRecipesUseCase,
    val deleteRecipe: DeleteRecipeUseCase,
    val validateInput: ValidateRecipeInputUseCase
)

@Module
@InstallIn(SingletonComponent::class)
object RecipeModule {

    @Provides
    @Singleton
    fun provideRepository(): RecipeRepository = RecipeRepositoryImpl()

    @Provides
    @Singleton
    fun provideRecipeUseCases(repository: RecipeRepository): RecipeUseCases {
        return RecipeUseCases(
            getRecipes = GetRecipesUseCase(repository),
            deleteRecipe = DeleteRecipeUseCase(repository),
            validateInput = ValidateRecipeInputUseCase(repository)
        )
    }
}

@HiltViewModel
class RecipeViewModel @Inject constructor(private val useCases: RecipeUseCases): ViewModel() {
    private var _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    fun loadRecipes() {
        viewModelScope.launch {
            _recipes.value = useCases.getRecipes()
        }
    }

    fun deleteRecipe(id: Int) {
        viewModelScope.launch {
            val result = useCases.deleteRecipe(id)
            if(result) loadRecipes()
        }
    }

    suspend fun validateRecipe(recipe: Recipe): Boolean {
        return withContext(Dispatchers.IO) {
            useCases.validateInput(recipe)
        }
    }
}

@AndroidEntryPoint
class BundleRecipeActivity: ComponentActivity() {
    val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    RecipeScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun RecipeScreen(viewModel: RecipeViewModel) {
    val recipeList by viewModel.recipes.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        Text(text = "Recipe List", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(recipeList) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onDeleteClick = { viewModel.deleteRecipe(recipe.id) }
                )
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column{
                Text(recipe.name, style = MaterialTheme.typography.titleMedium)
                Text("Ingredients: ${recipe.ingredients.joinToString(", ")}")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }

}





