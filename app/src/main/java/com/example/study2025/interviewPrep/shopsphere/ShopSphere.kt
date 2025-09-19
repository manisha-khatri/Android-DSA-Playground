package com.example.study2025.interviewPrep.shopsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.http.GET
import javax.inject.Inject

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

    @Insert
    fun insertSuggestion(suggestions: List<CachedSuggestion>)

    @Query("DELETE FROM cached_suggestion")
    fun deleteSuggestion()
}

@Database(
    version = 1,
    entities = [CachedSuggestion::class]
)
abstract class SuggestionDatabase: RoomDatabase() {
    abstract fun getSuggestionDao(): SearchSuggestionDao
}

//Mappers
/**
 * SearchSuggestionDto --> SearchSuggestion
 * SearchSuggestionDto --> CachedSuggestion
 * ProductResponse
 * ProductDto --> Product
 *
 * CachedSuggestion --> SearchSuggestion
 */

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
//----------------------------------

// repository
class SearchRepositoryImpl(val api: SearchApiService, val dao: SearchSuggestionDao): SuggestionRepository {
    override suspend fun fetchSuggestion(keyword: String): List<SearchSuggestion> {
        return try {
            val apiSuggestions = api.getSearchSuggestions(keyword)
            dao.insertSuggestion(apiSuggestions.map{ it.toCachedSuggestion() })
            apiSuggestions.map {it.toSearchSuggestion()}
        } catch(e: Exception) {
            val cachedSuggestion = dao.fetchSuggestion(keyword)
            cachedSuggestion.map {it.toSearchSuggestion() }
        }
    }

    override suspend fun getProducts(keyword: String): List<Product> {
        return try {
            val products = api.getProducts(keyword).products
            products.map {it.toProduct()}
        } catch(e: Exception) {
            throw e
        }
    }
}


// domain
data class Product(
    val id: String,
    val name: String,
    val imageUrl: String
)
data class SearchSuggestion(val suggestion: String)

interface SuggestionRepository {
    suspend fun fetchSuggestion(keyword: String): List<SearchSuggestion>
    suspend fun getProducts(keyword: String): List<Product>
}

// usecase
class GetSuggestionUseCase @Inject constructor(val repository: SuggestionRepository) {
    suspend operator fun invoke(keyword: String): List<SearchSuggestion> = repository.fetchSuggestion(keyword)
}

class GetProductUseCase @Inject constructor(val repository: SuggestionRepository) {
    suspend operator fun invoke(keyword: String): List<Product> = repository.getProducts(keyword)
}

//presentation

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    // Screen
                }
            }
        }
    }
}

data class SearchUiState(
    val query: String = "",
    val suggestions: List<SearchSuggestion> = emptyList(),
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isProductLoading: Boolean = false
)

sealed class SearchEvent {
    data class QueryChanged(val query: String): SearchEvent()
    data class SuggestionClicked(val suggestion: String): SearchEvent()
    data class ProductSearch(val suggestion: String): SearchEvent()
    object RetrySearch: SearchEvent()
    object ClearQuery: SearchEvent()
}


class SearchViewModel @Inject constructor(
    val getSuggestionUseCase: GetSuggestionUseCase,
    val getProductUseCase: GetProductUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onEvent() {

    }

}






