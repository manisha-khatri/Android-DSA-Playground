package com.example.systemdesign.interview.tesco

/**
import androidx.compose.runtime.*
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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
    version = 2
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

    override suspend fun getSearchSuggestions(query: String): List<SearchSuggestion> {
        return api.getSuggestions(query).take(5)
    }

    override suspend fun getProducts(query: String): List<Product> {
        dao.insertSuggestions(SearchHistoryEntity(query))
        return api.getProducts(query).products
    }

    override fun getRecentSearchHistory(): Flow<List<SearchSuggestion>> {
        return dao.getRecentSearches()
            .map { entities ->
                entities.map { SearchSuggestion(it.suggestion) }
            }
    }
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
        ).fallbackToDestructiveMigration()
            .build()

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
    suspend fun getSearchSuggestions(query: String): List<SearchSuggestion>
    suspend fun getProducts(query: String): List<Product>
    fun getRecentSearchHistory(): Flow<List<SearchSuggestion>>
}

// ====================================================================================
// UI LAYER
// ====================================================================================

data class SearchUiState(
    val query: String = "",
    val suggestions: List<SearchSuggestion> = emptyList(),
    val isSuggestionLoading: Boolean = false,
    val isProductLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)

sealed class SearchEvent {
    data class QueryChanged(val query: String) : SearchEvent()
    data class SuggestionClicked(val suggestion: String) : SearchEvent()
    object SearchBarFocused : SearchEvent()
    object ClearQuery : SearchEvent()
    object RetrySearch : SearchEvent()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val queryFlow = MutableStateFlow("")

    // Tracks if the user is currently looking at history/suggestions or products
    private val isSearching = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SearchUiState> = combine(
        queryFlow,
        isSearching
    ) { query, searching ->
        query to searching
    }
        .flatMapLatest { (query, searching) ->
            when {
                // 1. User triggered a search
                searching -> flow {
                    emit(SearchUiState(query = query, isProductLoading = true))
                    try {
                        val products = repository.getProducts(query)
                        emit(SearchUiState(query = query, products = products))
                    } catch (e: Exception) {
                        emit(SearchUiState(query = query, error = e.message))
                    }
                }

                // 2. Query is empty: Show Recent Search History
                query.isBlank() -> {
                    repository.getRecentSearchHistory()
                        .map { history ->
                            SearchUiState(query = query, suggestions = history)
                        }
                }

                // 3. User is typing: Show Suggestions
                else -> flow {
                    // Debounce is handled by the upstream queryFlow if preferred,
                    // but we'll assume it's handled here or via the UI.
                    emit(SearchUiState(query = query, isSuggestionLoading = true))
                    try {
                        val suggestions = repository.getSearchSuggestions(query)
                        emit(SearchUiState(query = query, suggestions = suggestions))
                    } catch (e: Exception) {
                        emit(SearchUiState(query = query, error = e.message))
                    }
                }
            }
        }
        .catch { e ->
            emit(SearchUiState(error = e.message ?: "Unknown Error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState()
        )

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                queryFlow.value = event.query
                isSearching.value = false // Back to suggestion mode
            }

            is SearchEvent.SuggestionClicked -> {
                queryFlow.value = event.suggestion
                isSearching.value = true // Trigger product search mode
            }

            SearchEvent.SearchBarFocused -> {
                if (queryFlow.value.isBlank()) {
                    isSearching.value = false
                }
            }

            SearchEvent.ClearQuery -> {
                queryFlow.value = ""
                isSearching.value = false
            }

            SearchEvent.RetrySearch -> {
                if (queryFlow.value.isNotBlank()) {
                    isSearching.value = true
                }
            }
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
    var hasFocusedOnce by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused && !hasFocusedOnce) {
                    hasFocusedOnce = true
                    onFocus()
                }
            },
        leadingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        placeholder = { Text("Search products…") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
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

**/