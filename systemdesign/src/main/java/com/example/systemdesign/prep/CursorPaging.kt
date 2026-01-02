package com.example.systemdesign.prep
/*

import androidx.paging.compose.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.paging.compose.collectAsLazyPagingItems
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.example.systemdesign.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import kotlinx.coroutines.delay
import java.io.IOException

*/
/**
📱 Sample App: Cursor Pagination with Paging 3
What this app shows:
- Cursor-based pagination (after cursor)
- Paging 3 (PagingSource)

{
"items": [
{ "id": "101", "title": "Item 101" }
],
"nextCursor": "101"
}

 *//*

//mocking

// This delay simulates network latency
private const val SIMULATED_NETWORK_DELAY = 500L
// This controls how many pages the mock API has
private const val MAX_PAGES = 15
// Number of items per page, matching PagingConfig
private const val ITEMS_PER_PAGE = 20

class MockItemApi : ItemApi {

    // A simple function to generate an item list for a given page/cursor
    private fun generateItems(startId: Int, limit: Int): List<Item> {
        return (0 until limit).map { index ->
            val id = startId + index
            Item(
                id = id.toString(),
                title = "Mock Item #$id"
            )
        }
    }

    override suspend fun getItems(limit: Int, after: String?): ItemResponse {
        delay(SIMULATED_NETWORK_DELAY)

        // 1. Determine the starting point (cursor)
        val startId = try {
            after?.toInt()?.plus(1) ?: 1 // Start at ID 1 for initial load (after=null)
        } catch (e: NumberFormatException) {
            // Handle invalid cursor format if necessary
            throw IOException("Invalid cursor format")
        }

        // Calculate the current page number for the MAX_PAGES check
        val currentPage = (startId - 1) / ITEMS_PER_PAGE

        // 2. Simulate end-of-data
        if (currentPage >= MAX_PAGES) {
            return ItemResponse(items = emptyList(), nextCursor = null)
        }

        // 3. Generate the data and new cursor
        val items = generateItems(startId, limit)
        val lastId = items.lastOrNull()?.id

        // Use the last ID as the next cursor
        val nextCursor = if (lastId != null && currentPage < MAX_PAGES - 1) {
            lastId
        } else {
            null // Last page reached
        }

        // Simulate an error on a specific page (e.g., page 3) for testing LoadState.Error
        */
/*
        if (currentPage == 2) {
            throw IOException("Mock API Error on Page 3")
        }
        *//*


        return ItemResponse(
            items = items,
            nextCursor = nextCursor
        )
    }
}

// Data
interface ItemApi {
    @GET("items")
    suspend fun getItems(
        @Query("limit") limit: Int,
        @Query("after") after: String?
    ): ItemResponse
}

class ItemPagingSource(private val api: ItemApi) : PagingSource<String, Item>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Item> {
        return try {
            val response = api.getItems(
                limit = params.loadSize, //
                after = params.key // response.nextCursor, Initial load: params.key = null
            )

            LoadResult.Page(
                data = response.items,
                prevKey = null,            // forward pagination only
                nextKey = response.nextCursor
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<String, Item>
    ): String? = null
}

// Domain
data class Item(
    val id: String,
    val title: String
)

data class ItemResponse(
    val items: List<Item>,
    val nextCursor: String?
)

class ItemRepository @Inject constructor(private val api: ItemApi) {
    fun getItems(): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ItemPagingSource(api) }
        ).flow
    }
}

// ui
data class ItemUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ItemViewModel @Inject constructor(
    repository: ItemRepository
) : ViewModel() {
    val pagingDataFlow: Flow<PagingData<Item>> =
        repository.getItems()
            .cachedIn(viewModelScope)
}

@Composable
fun ItemScreen(
    viewModel: ItemViewModel = hiltViewModel()
) {
    val items = viewModel.pagingDataFlow.collectAsLazyPagingItems()

    LazyColumn {
        items(items) { item ->
            item?.let {
                ItemRow(it)
            }
        }

        // Handle initial load/refresh loading state
        when (val state = items.loadState.refresh) {
            is LoadState.Loading -> {
                item { LoadingItem() }
            }
            is LoadState.Error -> {
                item { ErrorItem("Refresh failed: ${state.error.message ?: "Unknown Error"}") }
            }
            else -> { */
/* NotLoading *//*
 }
        }

        // Handle append (load more) loading state
        when (val state = items.loadState.append) {
            is LoadState.Loading -> {
                item { LoadingItem() }
            }
            is LoadState.Error -> {
                item { ErrorItem("Load more failed: ${state.error.message ?: "Unknown Error"}") }
            }
            else -> { */
/* NotLoading *//*
 }
        }
    }
}

@Composable
fun ItemRow(item: Item) {
    Text(
        text = item.title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorItem(message: String) {
    Text(
        text = message,
        color = Color.Red,
        modifier = Modifier.padding(16.dp)
    )
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideApi(retrofit: Retrofit): ItemApi {
        return if (BuildConfig.USE_MOCK_API) {
            MockItemApi()
        } else {
            retrofit.create(ItemApi::class.java)
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    ItemScreen()
                }
            }
        }
    }
}

@HiltAndroidApp
class MyApp : Application()


*/
