package com.example.systemdesign.prep.phonepe.moviedb.pagination
/**
import androidx.paging.compose.itemKey
import androidx.paging.compose.itemContentType
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import coil.compose.AsyncImage
import com.example.systemdesign.BuildConfig
import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

/**

Features:
1. Movie List Screen
 * Fetch **popular movies** from TMDB API
 * Display movies in a **vertical list**
 * Each item should show:
 * Movie poster
 * Movie name
 * Release date
 * Rating
 * Show a **loading indicator** while fetching data
 * Show **error UI** on failure
- Search Feature
 * Add a **search bar**
 * Search movies by name (API based search)
 * Update list as user searches
- Pagination
 * Implement **pagination** while scrolling the list
 * Load next page when user reaches bottom
- Caching
 * Cache movie list locally (Room / in-memory)
 * On app reopen:
 * Show cached data first
 * Then refresh from API

2. Movie Detail Screen
 * On clicking a movie, navigate to **Movie Detail Screen**
 * Show:
 * Large poster
 * Movie title
 * Overview / description
 * Rating
 * Release date
 */

// --------------------- DATA ----------------------

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey val id: Long,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Double
)

data class MovieListResponse(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int
)

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val movieId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)

data class MovieDetail(
    val id: Long,
    val title: String,
    val overview: String,
    val runtime: Long,
    val genres: List<Genre>,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("poster_path")
    val posterPath: String,
)

data class Genre(
    val id: Long,
    val name: String,
)

interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): MovieListResponse

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Long): MovieDetail
}

@Dao
interface MovieDao {
    @androidx.room.Query("SELECT * FROM movie ORDER BY releaseDate DESC")
    fun pagingSource(): PagingSource<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @androidx.room.Query("DELETE FROM movie")
    suspend fun clearAll()
}

@Dao
interface RemoteKeysDao {
    @androidx.room.Query("SELECT * FROM remote_keys WHERE movieId = :movieId")
    suspend fun remoteKeysMovieId(movieId: Long): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @androidx.room.Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}

@Database(
    entities = [Movie::class, RemoteKeys::class],
    version = 3
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val api: MovieApiService,
    private val db: MovieDatabase
) : RemoteMediator<Int, Movie>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1

            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                   ?: return MediatorResult.Success(endOfPaginationReached = true)

                db.remoteKeysDao()
                    .remoteKeysMovieId(lastItem.id)
                    ?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        try {
            val response = api.getPopularMovies(page)
            val movies = response.results
            val endReached = movies.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.movieDao().clearAll()
                }

                val keys = movies.map {
                    RemoteKeys(
                        movieId = it.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endReached) null else page + 1
                    )
                }

                db.remoteKeysDao().insertAll(keys)
                db.movieDao().insertAll(movies)
            }

            return MediatorResult.Success(endReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}

class MovieRepositoryImpl @Inject constructor(
    private val db: MovieDatabase,
    private val api: MovieApiService
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPopularMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(api, db),
            pagingSourceFactory = { db.movieDao().pagingSource() }
        ).flow

    override suspend fun searchMovies(query: String): List<Movie> =
        api.searchMovies(query).results

    override suspend fun getMovieDetails(movieId: Long): MovieDetail =
        api.getMovieDetails(movieId)
}

// --------------------- DOMAIN ----------------------

interface MovieRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun getMovieDetails(movieId: Long): MovieDetail
}

// --------------------- PRESENTATION ----------------------
/**
 * searchbar<>
 * popular list
 */
sealed interface HomePageUiState {
    object Loading : HomePageUiState
    class Error(val msg: String) : HomePageUiState
    data class Success(val movies: List<Movie>) : HomePageUiState
}

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val popularMovies: Flow<PagingData<Movie>> =
        repo.getPopularMovies()
            .cachedIn(viewModelScope)

    val searchResults: StateFlow<List<Movie>> =
        searchQuery
            .debounce(300)
            .filter { it.isNotBlank() }
            .mapLatest { repo.searchMovies(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onSearch(query: String) {
        searchQuery.value = query
    }
}

sealed interface MovieDetailUiState {
    object Loading : MovieDetailUiState
    class Error(val msg: String) : MovieDetailUiState
    data class Success(val data: MovieDetail) : MovieDetailUiState
}

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    val repo: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()
    private val movieId: Long = savedStateHandle["id"] ?: error("Movie id is missing")

    init {
        fetchMovieDetails(movieId)
    }

    fun fetchMovieDetails(movieId: Long) {
        viewModelScope.launch {
            try {
                val data = repo.getMovieDetails(movieId)
                _uiState.value = MovieDetailUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = MovieDetailUiState.Error(e.message.toString())
            }
        }
    }
}

// screens
@Composable
fun HomePageScreen(
    viewModel: HomePageViewModel = hiltViewModel(),
    onMovieClick: (Long) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val pagingMovies = viewModel.popularMovies.collectAsLazyPagingItems()
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.onSearch(it)
            },
            placeholder = { Text("Search movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (query.isBlank()) {

            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(
                    count = pagingMovies.itemCount,
                    key = pagingMovies.itemKey { it.id },
                    contentType = pagingMovies.itemContentType { "movie" }
                ) { index ->
                    pagingMovies[index]?.let {
                        MovieCard(it) { onMovieClick(it.id) }
                    }
                }

                pagingMovies.apply {
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        is LoadState.Error -> {
                            item(span = { GridItemSpan(2) }) {
                                Text("Failed to load movies")
                            }
                        }
                        else -> Unit
                    }
                }
            }

        } else {
            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(searchResults) {
                    MovieCard(it) { onMovieClick(it.id) }
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier.height(160.dp)
            )
            Text(movie.title, maxLines = 1)
        }
    }
}


@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (val curState = state) {
        is MovieDetailUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = curState.msg)
            }
        }

        MovieDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MovieDetailUiState.Success -> {
            val movie = curState.data
            Column(
                modifier = Modifier
                 .fillMaxSize()
                 .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
                    contentDescription = movie.title,
                    modifier = Modifier
                     .fillMaxWidth()
                     .height(280.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = movie.title,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Release date: ${movie.releaseDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = movie.genres.joinToString(" • ") { it.name },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

object Routes {
    const val HOME_PAGE = "home"
    const val MOVIE_DETAIL = "movie_detail"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME_PAGE,
    ) {
        composable(Routes.HOME_PAGE) {
            HomePageScreen(
                onMovieClick = { id ->
                    navController.navigate("${Routes.MOVIE_DETAIL}/${id}")
                }
            )
        }

        composable(
            route = "${Routes.MOVIE_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            MovieDetailScreen()
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold() { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    AppNavGraph()
                }
            }
        }
    }
}

@HiltAndroidApp
class MovieDBApp : Application()
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockMovieApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealMovieApi

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ---------- ROOM ----------
    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ): MovieDatabase =
        Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_db"
        ).build()

    // ---------- JSON ASSET READER ----------
    @Provides
    @Singleton
    fun provideJsonAssetReader(
        @ApplicationContext context: Context
    ): JsonAssetReader = JsonAssetReader(context)

    // ---------- REAL API ----------
    @Provides
    @Singleton
    @RealMovieApi
    fun provideRealApi(): MovieApiService =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)

    // ---------- MOCK API ----------
    @Provides
    @Singleton
    @MockMovieApi
    fun provideMockApi(
        jsonAssetReader: JsonAssetReader
    ): MovieApiService =
        MockMovieApiService(jsonAssetReader)

    // ---------- API SWITCH (ONLY unqualified binding) ----------
    @Provides
    @Singleton
    fun provideMovieApiService(
        @MockMovieApi mockApi: MovieApiService,
        @RealMovieApi realApi: MovieApiService
    ): MovieApiService =
        if (BuildConfig.USE_MOCK_API) mockApi else realApi

    // ---------- REPOSITORY ----------
    @Provides
    @Singleton
    fun provideRepo(db: MovieDatabase, api: MovieApiService): MovieRepository =
        MovieRepositoryImpl(db, api)
}

**/
