package com.example.systemdesign.interview.agoda

import android.app.Application
import android.os.Bundle
import androidx.compose.foundation.lazy.items
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
Home --> ProductList --> ProductDetail
 */

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

class GetProductsUseCase (private val repository: ProductRepository2) {
    suspend operator fun invoke() = repository.getProducts()
}

class GetProductDetailsUseCase (private val repo: ProductRepository2) {
    suspend operator fun invoke(id: Int) = repo.getProductDetails(id)
}

// ====================================================================================
// PRESENTATION LAYER
// ====================================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onBtnClick: () -> Unit) {
    val viewModel: HomeViewModel = viewModel()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Home Page") } ) })
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(text =  "Welcome to the Product App")
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { viewModel.onNavigateToListScreen(onBtnClick) }) {
                Text("View Products List")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    container: AppContainer,
    onNavigateToDetail: (id: Int) -> Unit
) {
    val viewModel: ListViewModel = viewModel(
        factory = ListViewModelFactory(container.getProductsUseCase)
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {TopAppBar(title = {Text("List Screen")})}) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.error.isNotEmpty() -> Text(text = uiState.error)
                else ->
                    Column{
                        LazyColumn {
                            items(items = uiState.items) { product ->
                                Column(
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            viewModel.onItemClick(product.id, { id -> onNavigateToDetail(id)})
                                        })
                                ) {
                                    Text(text = product.title)
                                }
                            }
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(container: AppContainer, navBackStackEntry: NavBackStackEntry) {
    val factory = DetailViewModelFactory(container.getProductDetailsUseCase, navBackStackEntry)
    val viewModel: DetailViewModel = viewModel(
        viewModelStoreOwner = navBackStackEntry,
        factory = factory
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {TopAppBar(title = {Text("Detail Screen")})}) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.error.isNotEmpty() -> Text(uiState.error)
                else -> Column {
                    val product = uiState.product
                    if(product != null) {
                        Text(text =  product.title)
                        Text(text =  product.description)
                    }
                }
            }
        }
    }
}

object Routes {
    const val HOME = "home"
    const val LIST = "list"
    const val DETAIL = "detail"
}

@Composable
fun AppNavGraph(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onBtnClick = { navController.navigate(Routes.LIST) }
            )
        }
        composable(Routes.LIST) {
            ListScreen(
                container = container,
                onNavigateToDetail = { productId ->
                    navController.navigate("${Routes.DETAIL}/$productId")
                }
            )
        }
        composable(
            route = "${Routes.DETAIL}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            DetailScreen(
                container = container,
                navBackStackEntry = backStackEntry
            )
        }
    }
}

class HomeViewModel : ViewModel() {
    fun onNavigateToListScreen(onNavigate: () -> Unit) = onNavigate()
}

class ListViewModelFactory(
    private val useCase: GetProductsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListViewModel(useCase) as T
    }
}

data class ListUiState(
    val items: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

class ListViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = _uiState.asStateFlow()

    init { loadProducts() }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading=true)
            try {
                val products = getProductsUseCase()
                _uiState.value = ListUiState(
                    items = products,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = ListUiState(error = e.message.toString(), isLoading = false)
            }
        }
    }

    fun onItemClick(id: Int, onNavigate: (Int) -> Unit) {
        onNavigate(id)
    }
}

class DetailViewModelFactory(private val useCase: GetProductDetailsUseCase, owner: NavBackStackEntry) :
    AbstractSavedStateViewModelFactory(owner as SavedStateRegistryOwner, owner.arguments) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(useCase, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class DetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)

class DetailViewModel(
    private val getDetailsUseCase: GetProductDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        val productId: Int? = savedStateHandle["productId"]
        if(productId!=null) {
            showProductDetails(productId)
        } else {
            _uiState.value = DetailUiState(error = "Product ID argument is missing.", isLoading = false)
        }
    }

    fun showProductDetails(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            try {
                val product = getDetailsUseCase(id)
                _uiState.value = DetailUiState(product = product, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = DetailUiState(error = e.message.toString(), isLoading = false)
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    private val container by lazy {
        (application as ProductApp).appContainer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavGraph(container)
        }
    }
}

class ProductApp : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }
}

// ====================================================================================
// HILT
// ====================================================================================
class AppContainer {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productApi: ProductAPI = retrofit.create(ProductAPI::class.java)

    val repository: ProductRepository2 = ProductRepositoryImpl(productApi)

    // usecases
    val getProductsUseCase = GetProductsUseCase(repository)
    val getProductDetailsUseCase = GetProductDetailsUseCase(repository)
}
