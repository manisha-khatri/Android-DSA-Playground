package com.example.systemdesign.prep.phonepe.moviedb.pagination
/**
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.collections.filter
import kotlin.math.ceil

class JsonAssetReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun readJson(fileName: String): String {
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}

class MockMovieApiService @Inject constructor(
    private val jsonAssetReader: JsonAssetReader
) : MovieApiService {

    private val gson = Gson()
    private val pageSize = 20

    private val allMovies: List<Movie> by lazy {
        val json = jsonAssetReader.readJson("popular_movies.json")
        gson.fromJson(json, MovieListResponse::class.java).results
    }

    override suspend fun getPopularMovies(page: Int): MovieListResponse {
        delay(300)

        val fromIndex = (page - 1) * pageSize
        val toIndex = minOf(fromIndex + pageSize, allMovies.size)

        val pagedMovies =
            if (fromIndex >= allMovies.size) emptyList()
            else allMovies.subList(fromIndex, toIndex)

        return MovieListResponse(
            page = page,
            results = pagedMovies,
            totalPages = ceil(allMovies.size / pageSize.toDouble()).toInt()
        )
    }

    override suspend fun searchMovies(query: String): MovieListResponse {
        val filtered = allMovies.filter {
            it.title.contains(query, ignoreCase = true)
        }
        return MovieListResponse(
            page = 1,
            results = filtered,
            totalPages = 1
        )
    }

    override suspend fun getMovieDetails(movieId: Long): MovieDetail {
        val json = jsonAssetReader.readJson("movie_details.json")
        val details: List<MovieDetail> =
            gson.fromJson(json, object : TypeToken<List<MovieDetail>>() {}.type)

        return details.first { it.id == movieId }
    }
}
**/
