package com.example.systemdesign.interview.tesco

import android.app.Application
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import coil.compose.rememberAsyncImagePainter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.String
import kotlin.collections.map
import kotlin.collections.take

/**
com.example.productsearch
│
├── data
│   ├── local
│   │   ├── dao
│   │   │   └── SearchHistoryDao.kt
│   │   ├── db
│   │   │   └── AppDatabase.kt
│   │   └── entity
│   │       └── SearchHistoryEntity.kt
│   │
│   ├── remote
│   │   └── api
│   │       └── ProductApiService.kt
│   │
│   ├── repository
│   │   └── ProductRepositoryImpl.kt
│   │
│   └── di
│       └── DataModule.kt
│
└── domain
└── model
├── Product.kt
├── SearchSuggestion.kt
└── SearchHistory.kt

 */


// ====================================================================================
// DATA LAYER
// ====================================================================================

//local
@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    val suggestion: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestions(entry: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<SearchHistoryEntity>>
}

@Database(
    entities = [SearchHistoryEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}

//remote
interface ProductApiService {
    @GET("suggestions")
    suspend fun getSuggestions(
        @retrofit2.http.Query("text") query: String = ""
    ): List<SearchSuggestion>

    @GET("products")
    suspend fun getProducts(
        @retrofit2.http.Query("query") query: String = ""
    ): ProductResponse
}

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService,
    private val dao: SearchHistoryDao
) : ProductRepository {

    override fun getSearchSuggestions(query: String): Flow<List<SearchSuggestion>> =
        flow { emit(api.getSuggestions(query).take(5)) }

    override fun getProducts(query: String): Flow<List<Product>> =
        flow {
            dao.insertSuggestions(SearchHistoryEntity(query))
            emit(api.getProducts(query).products)
        }

    override fun getRecentSearchHistory(): Flow<List<SearchSuggestion>> =
        dao.getRecentSearches().map { list ->
            list.map { SearchSuggestion(text = it.suggestion) }
        }.take(5)
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ProductApiService =
        Retrofit.Builder()
            .baseUrl("https://api.mockfly.dev/mocks/afc70459-bddc-4d16-b6b3-1b649eec78bc/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "product_db"
        ).build()

    @Provides
    fun provideHistoryDao(db: AppDatabase) =
        db.searchHistoryDao()


    @Singleton
    @Provides
    fun providesRepository(
        api: ProductApiService,
        dao: SearchHistoryDao
    ): ProductRepository = ProductRepositoryImpl(api, dao)
}


// ====================================================================================
// DOMAIN LAYER
// ====================================================================================

data class ProductResponse(
    val page: Int,
    val limit: Int,
    val total: Int,
    val products: List<Product>
)

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = ""    // JSON does NOT have category → must have default
)

data class SearchSuggestion(
    val text: String
)

interface ProductRepository {
    fun getSearchSuggestions(query: String): Flow<List<SearchSuggestion>>
    fun getProducts(query: String): Flow<List<Product>>
    fun getRecentSearchHistory(): Flow<List<SearchSuggestion>>
}

// ====================================================================================
// UI LAYER
// ====================================================================================

data class SearchUiState(
    val query: String = "",
    val suggestions: List<SearchSuggestion> = emptyList(),
    val isSuggestionLoading: Boolean = false,
    val error: String? = null,
    val isProductLoading: Boolean = false,
    val products: List<Product> = emptyList(),
)

sealed class SearchEvent {
    data class QueryChanged(val query: String) : SearchEvent()
    data class SuggestionClicked(val suggestion: String) : SearchEvent()
    object ClearQuery : SearchEvent()
    object RetrySearch : SearchEvent()
    object SearchBarFocused : SearchEvent()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    private var suggestionJob: Job? = null
    private var productJob: Job? = null

    init {
        observeQueryChanges()
    }

    private fun observeQueryChanges() {
        queryFlow
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isNotBlank()) {
                    fetchSuggestions(query)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> onQueryChanged(event.query)
            is SearchEvent.SuggestionClicked -> onSuggestionClicked(event.suggestion)
            is SearchEvent.ClearQuery -> onClearQuery()
            is SearchEvent.RetrySearch -> onRetrySearch()
            is SearchEvent.SearchBarFocused -> onSearchBarFocused()
        }
    }

    private fun onSearchBarFocused() {
        if (_uiState.value.query.isBlank()) {
            fetchRecentSearchHistory()
        }
    }

    private fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery, suggestions = emptyList(), isSuggestionLoading = false, error = null) }
        queryFlow.value = newQuery
    }

    private fun onSuggestionClicked(suggestion: String) {
        _uiState.update { it.copy(query = suggestion, suggestions = emptyList(), isSuggestionLoading = false, error = null) }
        //queryFlow.value = suggestion
        performProductSearch(suggestion)
    }

    private fun fetchSuggestions(query: String) {
        suggestionJob?.cancel()
        suggestionJob = viewModelScope.launch {
            repository.getSearchSuggestions(query)
                .onStart {
                    _uiState.update { it.copy(isSuggestionLoading = true) }
                }
                .catch { e ->
                    _uiState.update { it.copy(isSuggestionLoading = false, error = e.message) }
                }
                .collect { suggestions ->
                    _uiState.update {
                        it.copy(suggestions = suggestions, isSuggestionLoading = false)
                    }
                }
        }
    }

    private fun fetchRecentSearchHistory() {
        suggestionJob?.cancel()
        suggestionJob = viewModelScope.launch {
            repository.getRecentSearchHistory()
                .onStart {
                    _uiState.update { it.copy(isSuggestionLoading = true, suggestions = emptyList()) }
                }
                .catch { e ->
                    _uiState.update { it.copy(isSuggestionLoading = false, error = e.message) }
                }
                .collect { recent ->
                    _uiState.update {
                        it.copy(suggestions = recent, isSuggestionLoading = false)
                    }
                }
        }
    }

    private fun performProductSearch(query: String) {
        productJob?.cancel()

        productJob = viewModelScope.launch {
            repository.getProducts(query)
                .onStart {
                    _uiState.update { it.copy(isProductLoading = true,  suggestions = emptyList()) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(isProductLoading = false, error = e.message)
                    }
                }
                .collect { products ->
                    _uiState.update {
                        it.copy(products = products, isProductLoading = false)
                    }
                }
        }
    }

    private fun onClearQuery() {
        _uiState.update {
            it.copy(
                query = "",
                suggestions = emptyList(),
                isSuggestionLoading = false,
                error = null
            )
        }
        queryFlow.value = ""
    }

    private fun onRetrySearch() {
        val currentQuery = uiState.value.query
        if (currentQuery.isNotBlank()) {
            queryFlow.value = currentQuery
            performProductSearch(currentQuery)
        } else {
            fetchRecentSearchHistory()
        }
    }
}

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ---------------------- SEARCH BAR ----------------------------
        SearchBar(
            query = state.query,
            onQueryChange = { newQuery ->
                viewModel.onEvent(SearchEvent.QueryChanged(newQuery))
            },
            onSearch = { viewModel.onEvent(SearchEvent.SuggestionClicked(state.query)) },
            onFocus = { viewModel.onEvent(SearchEvent.SearchBarFocused) }
        )

        Spacer(Modifier.height(12.dp))

        // ---------------------- SUGGESTION PROGRESS --------------------
        if (state.isSuggestionLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
        }

        // ---------------------- SUGGESTIONS LIST ------------------------
        if (!state.isSuggestionLoading && state.suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                SuggestionsList(
                    suggestions = state.suggestions,
                    onSuggestionClick = { suggestion ->
                        viewModel.onEvent(SearchEvent.SuggestionClicked(suggestion))
                    }
                )
            }
        }

        // ---------------------- PRODUCT LOADING -------------------------
        if (state.isProductLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // ---------------------- PRODUCT GRID ----------------------------
        if (!state.isProductLoading && state.products.isNotEmpty()) {
            ProductGrid(products = state.products)
        }

        // ---------------------- ERROR MESSAGE ----------------------------
        state.error?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFocus: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocus()
                }
            },
        shape = RoundedCornerShape(24.dp),
        leadingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        placeholder = { Text("Search products...") },
        singleLine = true,
    )
}

@Composable
fun SuggestionsList(
    suggestions: List<SearchSuggestion>,
    onSuggestionClick: (String) -> Unit
) {
    LazyColumn {
        items(suggestions) { suggestion ->
            Text(
                text = suggestion.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSuggestionClick(suggestion.text) }
                    .padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProductGrid(products: List<Product>) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(products) { product ->
            ProductCard(product)
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f), // taller card for image + text
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            // Product Name
            Text(
                text = product.name.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Product Price
            Text(
                text = "₹${product.price ?: 0.0}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
class TescoApplication : Application()

