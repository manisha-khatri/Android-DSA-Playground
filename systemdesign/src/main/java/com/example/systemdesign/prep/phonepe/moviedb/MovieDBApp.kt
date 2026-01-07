package com.example.systemdesign.prep.phonepe.moviedb

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.collectAsState
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
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import coil.compose.AsyncImage
import com.example.systemdesign.BuildConfig
import com.example.systemdesign.prep.phonepe.moviedb.mock.JsonAssetReader
import com.example.systemdesign.prep.phonepe.moviedb.mock.MockMovieApiService
import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Route
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

data class MovieListResponse(
    val page: Long,
    val results: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Long,
    @SerializedName("total_results")
    val totalResults: Long,
)

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey val id: Long,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Double,
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
    @GET("/movie/popular")
    suspend fun getPopularMovies(): MovieListResponse

    @GET("/movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Long): MovieDetail

    @GET("/search/movie")
    suspend fun searchMovies(@Query("query") query: String): MovieListResponse
}

@Dao
interface MovieDao {
    @androidx.room.Query("SELECT * FROM movie")
    fun getPopularMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movie: List<Movie>)
}

@Database(
    version = 1,
    entities = [Movie::class]
)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}

class MovieRepositoryImpl @Inject constructor(
    val dao: MovieDao,
    val api: MovieApiService
): MovieRepository {
    override fun getPopularMovies(): Flow<List<Movie>> =
        dao.getPopularMovies()
            .onStart {
                try {
                    val movies = api.getPopularMovies()
                    dao.insertMovies(movies.results)
                } catch (e: Exception) {
                    Log.e("MovieRepository", "Failed to refresh movies", e)
                }
            }

    override suspend fun getMovieDetails(movieId: Long): MovieDetail {
        return api.getMovieDetails(movieId)
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        return api.searchMovies(query).results
    }
}

// --------------------- DOMAIN ----------------------

interface MovieRepository {
    fun getPopularMovies(): Flow<List<Movie>>
    suspend fun getMovieDetails(movieId: Long): MovieDetail
    suspend fun searchMovies(query: String): List<Movie>
}

// --------------------- PRESENTATION ----------------------
/**
 * searchbar<>
 * popular list
 */
sealed interface HomePageUiState {
    object Loading: HomePageUiState
    class Error(val msg: String): HomePageUiState
    data class Success(val movies: List<Movie>): HomePageUiState
}

@HiltViewModel
class HomePageViewModel @Inject constructor(val repo: MovieRepository): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val uiState: StateFlow<HomePageUiState> =
        _searchQuery
            .debounce(300)
            .flatMapLatest { query ->
                if(query.isBlank()) {
                    repo.getPopularMovies()
                } else {
                    flow { emit(repo.searchMovies(query)) }
                }
            }
            .map <List<Movie>, HomePageUiState> {
                HomePageUiState.Success(it)
            }
            .catch {
                emit(HomePageUiState.Error(it.message ?: "Something went wrong"))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                HomePageUiState.Loading
            )

    fun onSearch(query: String) {
        _searchQuery.value = query
    }
}

sealed interface MovieDetailUiState {
    object Loading: MovieDetailUiState
    class Error(val msg: String): MovieDetailUiState
    data class Success(val data: MovieDetail): MovieDetailUiState
}

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    val repo: MovieRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
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
    onProductDetailClick: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.onSearch(it)
            },
            placeholder = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        when (val curState = state) {
            is HomePageUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = curState.msg)
                }
            }

            HomePageUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomePageUiState.Success -> {
                if(curState.movies.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No movies found")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(curState.movies) { movie ->
                            MovieCard(
                                movie,
                                onClick = { onProductDetailClick(movie.id)}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
            Text(
                text =  movie.title,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Release: ${movie.releaseDate}",
                style = MaterialTheme.typography.bodySmall
            )
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
                    text =  movie.title,
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
    const val HOME_PAGE = "HOME PAGE"
    const val MOVIE_DETAIL = "MOVIE DETAIL"
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
                onProductDetailClick = { movieId ->
                    navController.navigate("${Routes.MOVIE_DETAIL}/${movieId}")
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
class MainActivity: ComponentActivity() {
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
class MovieDBApp: Application()

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
    fun provideRoomDB(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_db"
        ).build()

    @Provides
    @Singleton
    fun provideDao(db: MovieDatabase): MovieDao = db.getMovieDao()

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

    @Provides
    @Singleton
    fun provideJsonAssetReader(
        @ApplicationContext context: Context
    ): JsonAssetReader = JsonAssetReader(context)

    // ---------- API SWITCH ----------
    @Provides
    @Singleton
    fun provideMovieApiService(
        @MockMovieApi mockApi: MovieApiService,
        @RealMovieApi realApi: MovieApiService
    ): MovieApiService =
        if (BuildConfig.USE_MOCK_API) mockApi else realApi

    // ---------- REPO ----------
    @Provides
    @Singleton
    fun provideRepository(
        dao: MovieDao,
        api: MovieApiService
    ): MovieRepository =
        MovieRepositoryImpl(dao, api)
}

