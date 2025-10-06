package com.example.study2025.systemdesign.interview

/*

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.http.GET
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.Transaction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.String

// data

// network
interface SearchApiService {
    @GET("suggestion")
    suspend fun getSearchSuggestions(@retrofit2.http.Query("keyword") text: String): List<SearchSuggestionDto>

    @GET("products")
    suspend fun getProducts(@retrofit2.http.Query("suggestion") text: String): ProductResponse
}

data class SearchSuggestionDto(val text: String)
data class ProductResponse(val products: List<ProductDto>)
data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String
)

//cache
@Entity(tableName = "cached_suggestion")
data class CachedSuggestion(
    @PrimaryKey
    val suggestion: String
)

@Dao
interface SearchSuggestionDao {
    @Query("SELECT * FROM cached_suggestion WHERE suggestion LIKE '%' || :query || '%'")
    suspend fun fetchSuggestion(query: String): List<CachedSuggestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSuggestion(suggestions: List<CachedSuggestion>)

    @Query("DELETE FROM cached_suggestion")
    fun deleteSuggestion()

    @Transaction
    suspend fun clearAndInsert(suggestions: List<CachedSuggestion>) {
        deleteSuggestion()
        insertSuggestion(suggestions)
    }
}

@Database(
    version = 1,
    entities = [CachedSuggestion::class]
)
abstract class SuggestionDatabase: RoomDatabase() {
    abstract fun getSuggestionDao(): SearchSuggestionDao
}

fun SearchSuggestionDto.toSearchSuggestion(): SearchSuggestion = SearchSuggestion(
    suggestion = text
)

fun SearchSuggestionDto.toCachedSuggestion(): CachedSuggestion = CachedSuggestion(
    suggestion = text
)

fun CachedSuggestion.toSearchSuggestion(): SearchSuggestion = SearchSuggestion(
    suggestion = this.suggestion
)

fun ProductDto.toProduct(): Product = Product(
    id = this.id,
    name = this.name,
    imageUrl = this.imageUrl
)

class SearchRepositoryImpl @Inject constructor(val dao: SearchSuggestionDao, val api: SearchApiService): SuggestionRepository {
    override suspend fun fetchSuggestion(keyword: String): List<SearchSuggestion> {
        return try {
            val suggestions = api.getSearchSuggestions(keyword)
            dao.clearAndInsert(suggestions.map { it.toCachedSuggestion() })
            suggestions.map { it.toSearchSuggestion()}
        } catch(e: Exception) {
            val suggestions = dao.fetchSuggestion(keyword)
            suggestions.map { it.toSearchSuggestion() }
        }
    }

    override suspend fun getProducts(suggestion: String): List<Product> {
        return try {
            val products = api.getProducts(suggestion).products
            products.map {it.toProduct()}
        } catch(e: Exception) {
            throw e
        }
    }
}
//----------------------------------
data class SearchSuggestion(
    val suggestion: String
)

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String
)

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Failure(val e: Throwable): Result<Nothing>()
}

interface SuggestionRepository {
    suspend fun fetchSuggestion(keyword: String): List<SearchSuggestion>
    suspend fun getProducts(suggestion: String): List<Product>
}

class GetSuggestionUseCase @Inject constructor(val repo: SuggestionRepository) {
    suspend operator fun invoke(keyword: String): List<SearchSuggestion> = repo.fetchSuggestion(keyword)
}

class GetProductUseCase @Inject constructor(val repo: SuggestionRepository) {
    suspend operator fun invoke(suggestion: String): List<Product> = repo.getProducts(suggestion)
}

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val suggestions: List<SearchSuggestion> = emptyList(),
    val isProductLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)

sealed class SearchEvent() {
    data class QueryChanged(val query: String): SearchEvent()
    data class SuggestionClicked(val suggestion: String): SearchEvent()
}

@HiltViewModel
class SearchViewModel(
    val getSuggestionUseCase: GetSuggestionUseCase,
    val getProductUseCase: GetProductUseCase
): ViewModel() {

    val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.QueryChanged -> onQueryChanged(event.query)
            is SearchEvent.SuggestionClicked -> onProductSearch(event.suggestion)
        }
    }

    var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        _uiState.update{ it.copy(query = query, isLoading = true, error = null) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try{
                val suggestions =
                    if(query.isBlank()) emptyList()
                    else getSuggestionUseCase(query)

                _uiState.update{ it.copy(suggestions = suggestions, isLoading = false) }
            } catch(e: Exception) {
                _uiState.update{ it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onProductSearch(suggestion: String) {
        _uiState.update{ it.copy(isProductLoading= true)}

        searchJob = viewModelScope.launch {
            try {
                val products =
                    if(suggestion.isBlank()) emptyList()
                    else getProductUseCase(suggestion)

                _uiState.update{ it.copy(isProductLoading= false, products = products)}
            } catch(e: Exception) {
                _uiState.update{ it.copy(isProductLoading= false, error = e.message)}
            }
        }
    }
}

@Composable
fun SearchScreen(viewModel:SearchViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = state.query,
            onQueryChange = {
                viewModel.onEvent(SearchEvent.QueryChanged(it))
            }
        )
        if(state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if(state.suggestions.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                SuggestionList(
                    state.suggestions,
                    { viewModel.onEvent(SearchEvent.SuggestionClicked(it)) }
                )
            }
        }

        if(state.isProductLoading) CircularProgressIndicator()

        if(state.products.isNotEmpty()) {
            ProductList(state.products)
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange:(String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = {
            onQueryChange(it)
        },
        placeholder = {
            Text( text = "Search ...")
        }
    )
}

@Composable
fun SuggestionList(suggestions: List<SearchSuggestion>, onClick:(String) -> Unit) {
    Box {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(suggestions) {
                SuggestionItem(it, onClick)
            }
        }
    }
}

@Composable
fun SuggestionItem(suggestion: SearchSuggestion, onClick: (String) -> Unit) {
    Text(
        text = suggestion.suggestion,
        modifier = Modifier
            .padding(16.dp)
            .clickable{ onClick(suggestion.suggestion) }
    )
}

@Composable
fun ProductList(products: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(products) {
            Text(
                text = it.name,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    SearchScreen()
                }
            }
        }
    }
}

@HiltAndroidApp
class SearchApplication: Application()


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesSearchApiService(retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): SuggestionDatabase {
        return Room.databaseBuilder(
            context,
            SuggestionDatabase::class.java,
            "suggestion_db"
        ).build()
    }

    @Singleton
    @Provides
    fun providesSearchSuggestionDao(db: SuggestionDatabase): SearchSuggestionDao {
        return db.getSuggestionDao()
    }

    @Singleton
    @Provides
    fun providesSearchRepository(dao: SearchSuggestionDao, api: SearchApiService): SearchRepositoryImpl {
        return SearchRepositoryImpl(dao, api)
    }
}


 */