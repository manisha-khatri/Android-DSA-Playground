package com.example.systemdesign.prep.agoda

/**

import android.app.Application
import android.os.Bundle
import androidx.compose.foundation.lazy.items
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 Home --> ProductList --> ProductDetail
 */
object AppContainer {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ProductAPI = retrofit.create(ProductAPI::class.java)
    val repository: ProductRepository2 = ProductRepositoryImpl(api)

    val getProductsUC = GetProductsUseCase(repository)
    val getProductDetailsUC = GetProductDetailsUseCase(repository)
}

// ====================================================================================
// DATA LAYER
// ====================================================================================

interface ProductAPI {
    @GET("posts")
    suspend fun getProducts(): List<ProductDTO>

    @GET("posts/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): ProductDTO
}

data class ProductDTO(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

class ProductRepositoryImpl(val api: ProductAPI): ProductRepository2 {
    override suspend fun getProducts(): List<Product> {
        return api.getProducts().map { it.toProduct() }
    }

    override suspend fun getProductDetails(productId: Int): Product {
        return api.getProductDetails(productId).toProduct()
    }
}

fun ProductDTO.toProduct() = Product(
    id = id,
    title = title,
    description = body
)

// ====================================================================================
// DOMAIN LAYER
// ====================================================================================

data class Product(
    val id: Int,
    val title: String,
    val description: String,
)

interface ProductRepository2 {
    suspend fun getProducts(): List<Product>
    suspend fun getProductDetails(productId: Int): Product
}

class GetProductsUseCase(private val repository: ProductRepository2) {
    suspend operator fun invoke() = repository.getProducts()
}

class GetProductDetailsUseCase(private val repo: ProductRepository2) {
    suspend operator fun invoke(id: Int) = repo.getProductDetails(id)
}

// ====================================================================================
// PRESENTATION LAYER
// ====================================================================================

data class ListUiState(
    val items: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

class HomeViewModel : ViewModel() {
    fun goToList(onNavigate: () -> Unit) = onNavigate()
}

class ListViewModel(private val getProductsUC: GetProductsUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val products = getProductsUC()
                _uiState.value = ListUiState(items = products, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = ListUiState(error = e.message ?: "Error occurred")
            }
        }
    }

    fun onProductClick(id: Int, onNavigate: (Int) -> Unit) {
        onNavigate(id)
    }
}

data class DetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)

class DetailViewModel(private val getDetailsUC: GetProductDetailsUseCase, private val productId: Int) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDetails()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            try {
                val product = getDetailsUC(productId)
                _uiState.value = DetailUiState(product = product)
            } catch (e: Exception) {
                _uiState.value = DetailUiState(error = e.message ?: "Error loading")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, onNavigateToList: () -> Unit) {
    Scaffold(topBar = { TopAppBar({ Text("Home Page") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Text("Welcome to Products App")
            Spacer(Modifier.height(24.dp))
            Button(onClick = { viewModel.goToList(onNavigateToList) }) {
                Text("View Products")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(viewModel: ListViewModel, onNavigateToDetail: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { TopAppBar({ Text("Products List") }) }) { padding ->
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error.isNotEmpty() -> Text(uiState.error)
            else -> LazyColumn(Modifier.padding(padding)) {
                items(uiState.items) { product ->
                    Text(product.title,
                        modifier = Modifier
                            .clickable { viewModel.onProductClick(product.id, onNavigateToDetail) }
                            .padding(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { TopAppBar({ Text("Product Detail") }) }) { padding ->
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error.isNotEmpty() -> Text(uiState.error)
            else -> uiState.product?.let {
                Column(Modifier.padding(16.dp)) {
                    Text(it.title, style = MaterialTheme.typography.titleLarge)
                    Text(it.description)
                }
            }
        }
    }
}

class ListVMFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListViewModel(AppContainer.getProductsUC) as T
    }
}

class DetailVMFactory(private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(AppContainer.getProductDetailsUC, id) as T
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf("HOME") }
            var selectedId by remember { mutableStateOf(0) }

            when(currentScreen) {
                "HOME" -> HomeScreen(
                    viewModel = ViewModelProvider(this)[HomeViewModel::class.java],
                    onNavigateToList = { currentScreen = "LIST" }
                )

                "LIST" -> ListScreen(
                    viewModel = ViewModelProvider(this, ListVMFactory())[ListViewModel::class.java],
                    onNavigateToDetail = { id ->
                        selectedId = id
                        currentScreen = "DETAIL"
                    }
                )

                "DETAIL" -> DetailScreen(
                    viewModel = ViewModelProvider(this, DetailVMFactory(selectedId))[DetailViewModel::class.java]
                )
            }
        }
    }
}

class ProductApp : Application()


 */