package com.example.systemdesign.interview.agoda

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.Module
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

/**
 Home --> ProductList --> ProductDetail

 "How does Clean Architecture fit into designing an app with three screens
 — Home, List, and Details?
 Explain how you would structure the app, what modules you would create,
 and how you'd apply MVVM and theming. Just outline the interfaces and modules;
 no need for full implementation. We can discuss the details later."
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

class ProductRepositoryImpl @Inject constructor(val api: ProductAPI): ProductRepository2 {
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

class GetProductsUseCase @Inject constructor(private val repository: ProductRepository2) {
    suspend operator fun invoke() = repository.getProducts()
}

class GetProductDetailsUseCase @Inject constructor(private val repo: ProductRepository2) {
    suspend operator fun invoke(id: Int) = repo.getProductDetails(id)
}

// ====================================================================================
// HILT
// ====================================================================================

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // FULL logs
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesProductApi(retrofit: Retrofit): ProductAPI =
        retrofit.create(ProductAPI::class.java)

    @Provides
    fun providesRepository(api: ProductAPI): ProductRepository2 =
        ProductRepositoryImpl(api)
}

// ====================================================================================
// PRESENTATION LAYER
// ====================================================================================

data class ListUiState(
    val items: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val _navigateToList = MutableSharedFlow<Unit>()
    val navigateToList = _navigateToList.asSharedFlow()

    fun onViewProductsClicked() {
        viewModelScope.launch {
            _navigateToList.emit(Unit)
        }
    }
}

sealed interface ListUiEffect {
    data class NavigateToDetail(val productId: Int) : ListUiEffect
}

@HiltViewModel
class ListViewModel @Inject constructor(val getProductsUseCase: GetProductsUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ListUiEffect>()
    val uiEffect: SharedFlow<ListUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadProducts()
    }

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

    fun onProductSelected(id: Int) {
        viewModelScope.launch {
            _uiEffect.emit(ListUiEffect.NavigateToDetail(id))
        }
    }
}

data class DetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    val getDetailsUseCase: GetProductDetailsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToList: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.navigateToList.collect {
            onNavigateToList()
        }
    }

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
            Button(onClick = { viewModel.onViewProductsClicked() }) {
                Text("View Products List")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    onNavigateToDetail: (id: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ListUiEffect.NavigateToDetail -> {
                    onNavigateToDetail(effect.productId)
                }
            }
        }
    }

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
                                            viewModel.onProductSelected(product.id)
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
fun DetailScreen(viewModel: DetailViewModel = hiltViewModel()) {
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
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToList = { navController.navigate(Routes.LIST) }
            )
        }
        composable(Routes.LIST) {
            ListScreen(
                onNavigateToDetail = { productId ->
                    navController.navigate("${Routes.DETAIL}/$productId")
                }
            )
        }
        composable(
            route = "${Routes.DETAIL}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            DetailScreen()
        }
    }
}

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavGraph()
        }
    }
}

@HiltAndroidApp
class ProductApp: Application()

**/