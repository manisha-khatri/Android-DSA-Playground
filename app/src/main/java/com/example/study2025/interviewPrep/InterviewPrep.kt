package com.example.study2025.interviewPrep

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.http.GET
import javax.inject.Inject

/**
 * Write a suspend function using Retrofit and Kotlin Flow to fetch a
 * product list from a REST API and expose it to the UI.
 */

data class Product(val id:Int,  val name: String)

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}

class ProductRepository @Inject constructor(
    private val api: ProductApiService
) {
    fun fetchProducts(): Flow<List<Product>> = flow {
        val products = api.getProducts()
        emit(products)
    }.flowOn(Dispatchers.IO) // Do network on IO thread
}

sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    class Error(val msg: String): UiState<Nothing>()
}


@HiltViewModel
class ProductViewmodel @Inject constructor(val repository: ProductRepository): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Product>>> = _uiState

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            repository.fetchProducts()
                .catch { e ->
                    _uiState.emit(UiState.Error(e.message.toString()))
                }
                .collect { products ->
                    _uiState.emit(UiState.Success(products))
                }
        }
    }
}

@Composable
fun ProductScreen(viewModel: ProductViewmodel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    when(val uiState = state.value) {
        is UiState.Loading -> Text("Loading..")
        is UiState.Success -> ProductList(uiState.data)
        is UiState.Error -> Text("Error Occured ${uiState.msg}")
    }
}

@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { item ->
            Text(text = item.name)
        }
    }
}











