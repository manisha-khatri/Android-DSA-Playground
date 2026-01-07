package com.example.systemdesign.prep.phonepe.moviedb.pagination

import android.content.Context
import com.example.systemdesign.prep.phonepe.moviedb.MovieApiService
import com.example.systemdesign.prep.phonepe.moviedb.MovieDetail
import com.example.systemdesign.prep.phonepe.moviedb.MovieListResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject


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

    override suspend fun getPopularMovies(): MovieListResponse {
        delay(500) // simulate network
        val json = jsonAssetReader.readJson("popular_movies.json")
        return gson.fromJson(json, MovieListResponse::class.java)
    }

    override suspend fun getMovieDetails(movieId: Long): MovieDetail {
        delay(300)
        val json = jsonAssetReader.readJson("movie_details.json")

        val movies: List<MovieDetail> =
            gson.fromJson(json, object : TypeToken<List<MovieDetail>>() {}.type)

        return movies.firstOrNull { it.id == movieId }
            ?: throw IllegalArgumentException("Movie not found")
    }

    override suspend fun searchMovies(query: String): MovieListResponse {
        val popular = getPopularMovies()
        val filtered = popular.results.filter {
            it.title.contains(query, ignoreCase = true)
        }

        return popular.copy(results = filtered)
    }
}

