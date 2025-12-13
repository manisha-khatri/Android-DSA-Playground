package com.example.systemdesign.interview.tesco

import com.example.systemdesign.BuildConfig
import android.app.Application
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.systemdesign.interview.tesco.mock.JsonAssetReader
import com.example.systemdesign.interview.tesco.mock.MockSearchApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Design the business logic for a product search feature that supports multiple categories.
 * When a user comes back to the search screen and does not type anything, the system should
 * show their previous search suggestions.
 * Also handle ambiguous search terms, for example the word “apple”, which can refer to a
 * fruit or a mobile phone brand, and return results for both types appropriately.
 */

// ====================================================================================
// Data LAYER
// ====================================================================================

// local
@Entity(tableName = "search_suggestion")
data class SearchHistoryEntity (
    @PrimaryKey val suggestion: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(history: SearchHistoryEntity)

    @Query("SELECT * FROM search_suggestion ORDER BY timestamp DESC LIMIT 10")
    fun fetchSearchHistory(): Flow<List<SearchHistoryEntity>>
}

@Database(
    entities = [SearchHistoryEntity::class],
    version = 4
)
abstract class SearchDatabase: RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}

//remote
interface SearchApiService {
    @GET("suggestions")
    suspend fun fetchSuggestions(
        @retrofit2.http.Query("text") query: String = ""
    ): List<SearchSuggestion>

    @GET("products")
    suspend fun fetchProducts(
        @retrofit2.http.Query("query") query: String = ""
    ): CategorizedProductResponse
}

class SearchRepositoryImpl @Inject constructor(
    val api: SearchApiService,
    val dao: SearchHistoryDao
): SearchRepository {
    override fun fetchSearchSuggestion(query: String): Flow<List<SearchSuggestion>> =
        flow {
            emit(api.fetchSuggestions(query).take(5))
        }.flowOn(Dispatchers.IO)

    override fun fetchRecentSearches(): Flow<List<SearchSuggestion>> =
        dao.fetchSearchHistory().map { list ->
            list.map {
                SearchSuggestion(text = it.suggestion)
            }.take(5)
        }

    override fun fetchProducts(query: String): Flow<List<CategoryData>> = flow {
        dao.insertSearchHistory(SearchHistoryEntity(suggestion = query))
        emit(api.fetchProducts(query).categories)
    }.flowOn(Dispatchers.IO)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealApi


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ---------------- REAL API ----------------
    @Provides
    @Singleton
    @RealApi
    fun provideRealApiService(): SearchApiService =
        Retrofit.Builder()
            .baseUrl("https://api.mockfly.dev/mocks/afc70459-bddc-4d16-b6b3-1b649eec78bc/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApiService::class.java)

    // ---------------- MOCK API ----------------
    @Provides
    @Singleton
    @MockApi
    fun provideMockApiService(
        jsonAssetReader: JsonAssetReader
    ): SearchApiService {
        return MockSearchApiService(jsonAssetReader)
    }

    // ---------------- FINAL API (used everywhere) ----------------
    @Provides
    @Singleton
    fun provideApiService(
        @MockApi mockApi: SearchApiService,
        @RealApi realApi: SearchApiService
    ): SearchApiService {
        return if (BuildConfig.USE_MOCK_API) mockApi else realApi
    }

    // ---------------- DATABASE ----------------
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ): SearchDatabase =
        Room.databaseBuilder(
            context,
            SearchDatabase::class.java,
            "search_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideHistory(
        db: SearchDatabase
    ): SearchHistoryDao = db.searchHistoryDao()

    // ---------------- REPOSITORY ----------------
    @Provides
    @Singleton
    fun providesRepository(
        api: SearchApiService,
        dao: SearchHistoryDao
    ): SearchRepository =
        SearchRepositoryImpl(api, dao)
}


// ====================================================================================
// Domain LAYER
// ====================================================================================
data class CategoryData(
    val category: String,
    val products: List<Product>
)

data class CategorizedProductResponse(
    val categories: List<CategoryData>
)

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = ""
)

data class SearchSuggestion(
    val text: String
)

interface SearchRepository {
    fun fetchSearchSuggestion(query: String): Flow<List<SearchSuggestion>>
    fun fetchRecentSearches(): Flow<List<SearchSuggestion>>
    fun fetchProducts(query: String): Flow<List<CategoryData>>
}


// ====================================================================================
// UI LAYER
// ====================================================================================

data class SearchUiState(
    val query: String = "",
    val categorizedProducts: List<CategoryData> = emptyList(),
    val suggestions: List<SearchSuggestion> = emptyList(),
    val isProductLoading: Boolean = false,
    val isSuggestionLoading: Boolean = false,
    val error: String? = null
)

sealed class SearchEvent {
    data class QueryChanged(val query: String): SearchEvent()
    data class SuggestionClicked(val suggestion: String): SearchEvent()
    object SearchBarFocused: SearchEvent()
}

@HiltViewModel
class SearchViewModel @Inject constructor(val repository: SearchRepository): ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    private var suggestionJob: Job? = null
    private var productsJob: Job? = null

    init {
        observeQueryChanges()
    }

    private fun observeQueryChanges() {
        queryFlow
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                if(query.isNotBlank()) {
                    fetchSuggestions(query)
                } else {
                    _uiState.update { it.copy(suggestions = emptyList(), isSuggestionLoading = false)}
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.QueryChanged -> onQueryChanged(event.query)
            is SearchEvent.SuggestionClicked -> onSuggestionClicked(event.suggestion)
            is SearchEvent.SearchBarFocused -> onSearchBarFocused()
        }
    }

    fun onSearchBarFocused() {
        if(_uiState.value.query.isBlank()) {
            fetchRecentHistory()
        }
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery, suggestions = emptyList())}
        queryFlow.value = newQuery
    }

    fun onSuggestionClicked(suggestion: String) {
        // saveSearchHistory(suggestion)
        _uiState.update { it.copy(query = suggestion, suggestions = emptyList(), isSuggestionLoading = false)}
        fetchProducts(suggestion)
    }

    private fun fetchSuggestions(query: String) {
        suggestionJob?.cancel()
        suggestionJob = viewModelScope.launch {
            repository.fetchSearchSuggestion(query)
                .onStart {
                    _uiState.update { it.copy(isSuggestionLoading = true, suggestions = emptyList()) }
                }
                .catch { e ->
                    _uiState.update { it.copy(isSuggestionLoading = false, error = e.stackTraceToString()) }
                }
                .collect { suggestions ->
                    _uiState.update {
                        it.copy(suggestions = suggestions, isSuggestionLoading = false)
                    }
                }
        }
    }

    private fun fetchProducts(query: String) {
        productsJob?.cancel()
        productsJob = viewModelScope.launch {
            repository.fetchProducts(query)
                .onStart {
                    _uiState.update {
                        it.copy(isProductLoading = true, categorizedProducts = emptyList())
                    }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(isProductLoading = false, error = e.stackTraceToString())
                    }
                }
                .collect { categories ->
                    _uiState.update {
                        it.copy(
                            categorizedProducts = categories,
                            isProductLoading = false
                        )
                    }
                }
        }
    }

    private fun fetchRecentHistory() {
        suggestionJob?.cancel()
        suggestionJob = viewModelScope.launch {
            repository.fetchRecentSearches()
                .onStart {
                    _uiState.update { it.copy(isSuggestionLoading = true, suggestions = emptyList()) }
                }
                .catch { e ->
                    _uiState.update { it.copy(isSuggestionLoading = false, error = e.stackTraceToString()) }
                }
                .collect { suggestions ->
                    _uiState.update {
                        it.copy(suggestions = suggestions, isSuggestionLoading = false)
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
            fetchProducts(currentQuery)
        } else {
            fetchRecentHistory()
        }
    }
}

// UI
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

        // Search Bar
        SearchBar(
            query = state.query,
            onQueryChange = { viewModel.onEvent(SearchEvent.QueryChanged(it)) },
            onSearch = { viewModel.onEvent(SearchEvent.SuggestionClicked(state.query)) },
            onFocus = { viewModel.onEvent(SearchEvent.SearchBarFocused) }
        )

        Spacer(Modifier.height(12.dp))

        // Suggestions
        if (state.isSuggestionLoading)
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

        if (!state.isSuggestionLoading && state.suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                SuggestionsList(
                    suggestions = state.suggestions,
                    onSuggestionClicked = {
                        viewModel.onEvent(SearchEvent.SuggestionClicked(it))
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Products List
        when {
            state.isProductLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            state.categorizedProducts.isNotEmpty() -> {
                ProductList(
                    categoryList = state.categorizedProducts,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Error
        state.error?.let {
            Text(text = it, color = Color.Red)
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
                if(focusState.isFocused) {
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
    onSuggestionClicked: (String) -> Unit
) {
    LazyColumn {
        items(suggestions) { suggestion ->
            Text(
                text =  suggestion.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ onSuggestionClicked(suggestion.text) }
                    .padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProductList(
    categoryList: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        categoryList.forEach { categoryData ->

            // Category title
            item {
                Text(
                    text = categoryData.category.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Grid
            items(categoryData.products.chunked(2)) { rowItems ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { product ->
                        ProductCard(
                            product = product,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }

                    // If only 1 item, fill empty space
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "₹${product.price}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
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
class TescoApplication: Application()
