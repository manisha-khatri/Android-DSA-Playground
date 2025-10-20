package com.example.systemdesign.interview

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Consider an app with three screens: a Home screen, a List screen, and a List Details screen.
 * How would the app be architected using MVVM with Clean Architecture? Please outline the high-level module
 * structure and the main interfaces/classes for each layer without deep implementation details, so we can iterate on them later.
 *
 */

// presentation

/**
 * HomeScreen --> button
 * ListScreen --> Item List (ProductListState)
 * DetailScreen --> Item Details (ProductDetailState)
 */


/**
 * Full implementation of the three-screen app (Home, List, Detail)
 * structured using MVVM with Clean Architecture (Domain, Data, Presentation).
 */

// ====================================================================================
// DOMAIN LAYER
// ====================================================================================

// --- 1. Domain Model ---
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String = "",
    val description: String = ""
) {
    val formattedPrice: String
        get() = "$${String.format("%.2f", price)}"
}

// --- 2. Repository Interface (Contract for Data Layer) ---
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductDetails(productId: String): Result<Product>
}

// --- 3. Use Cases (Application Business Rules) ---
class GetProductListUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return repository.getProducts()
    }
}

class GetProductDetailsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Result<Product> {
        return repository.getProductDetails(productId)
    }
}

// ====================================================================================
// DATA LAYER
// ====================================================================================

// --- Data Models and Mappers ---
data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val image_url: String,
    val long_description: String
)

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        price = this.price,
        imageUrl = this.image_url,
        description = this.long_description
    )
}

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        price = this.price,
        imageUrl = this.imageUrl,
        description = this.description
    )
}

// --- Data Sources (Interfaces) ---
// Note: These interfaces are defined for structure but are not fully implemented (mocked below)
@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): ProductEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)
}

interface ProductApiService {
    suspend fun getProductList(): List<ProductDto>
    suspend fun getProductDetails(id: String): ProductDto
}

// --- Repository Implementation (Uses Mock Data) ---
class ProductRepositoryImpl @Inject constructor(
    // In a real app, ProductDao and ProductApiService would be injected here.
) : ProductRepository {

    // Mock data for demonstration
    private val mockProducts = listOf(
        Product("1", "Laptop Pro", 1299.00, description = "A high-performance laptop."),
        Product("2", "Smartphone X", 799.50, description = "The latest flagship phone."),
        Product("3", "Watch SE", 249.99, description = "Smartwatch with health features.")
    )

    override suspend fun getProducts(): Result<List<Product>> {
        // Simulate network/db delay
        delay(1000)
        return Result.success(mockProducts)
    }

    override suspend fun getProductDetails(productId: String): Result<Product> {
        delay(500)
        val product = mockProducts.find { it.id == productId }
        return if (product != null) {
            Result.success(product.copy(description = "Detailed description for $productId: ${product.description}"))
        } else {
            Result.failure(NoSuchElementException("Product with ID $productId not found."))
        }
    }
}

// ====================================================================================
// PRESENTATION LAYER
// ====================================================================================

// --- UI State Definitions ---
sealed class ProductListState {
    data object Initial: ProductListState()
    data object Loading: ProductListState()
    data class Success(val products: List<Product>): ProductListState()
    data class Error(val msg: String): ProductListState()
}

sealed class ProductDetailState {
    data object Initial: ProductDetailState()
    data object Loading: ProductDetailState()
    data class Success(val product: Product): ProductDetailState()
    data class Error(val msg: String): ProductDetailState()
}

// --- ViewModels ---
@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    fun onNavigateToListScreen(onNavigate: () -> Unit) {
        onNavigate()
    }
}

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getProductListUseCase: GetProductListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ProductListState>(ProductListState.Initial)
    val state: StateFlow<ProductListState> = _state.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = ProductListState.Loading
            getProductListUseCase()
                .onSuccess { products ->
                    _state.value = ProductListState.Success(products)
                }
                .onFailure { e ->
                    _state.value = ProductListState.Error("Failed to load products: ${e.message}")
                }
        }
    }

    fun onProductSelected(id: String, onNavigate: (String)-> Unit) {
        onNavigate(id)
    }
}

@HiltViewModel
class ListDetailViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow<ProductDetailState>(ProductDetailState.Initial)
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    init {
        // Retrieve navigation argument
        val productId: String? = savedStateHandle["productId"]
        if (productId != null) {
            loadProductDetails(productId)
        } else {
            _state.value = ProductDetailState.Error("Product ID argument is missing.")
        }
    }

    private fun loadProductDetails(id: String) {
        viewModelScope.launch {
            _state.value = ProductDetailState.Loading
            getProductDetailsUseCase(id)
                .onSuccess { product ->
                    _state.value = ProductDetailState.Success(product)
                }
                .onFailure { e ->
                    _state.value = ProductDetailState.Error("Failed to load details: ${e.message}")
                }
        }
    }
}

// --- Composables ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToList: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("App Home") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to the Product App!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.onNavigateToListScreen(onNavigateToList) }
            ) {
                Text("View Products List")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    onNavigateToDetail: (productId: String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Products") }) }
    ) { padding ->
        when (val currentState = state) {
            ProductListState.Loading, ProductListState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ProductListState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${currentState.msg}")
                }
            }
            is ProductListState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(
                        items = currentState.products,
                        key = { it.id } // Correctly using the ID as a key
                    ) { product ->
                        ProductListItem(
                            product = product,
                            onClick = {
                                viewModel.onProductSelected(product.id, onNavigateToDetail)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun ProductListItem(product: Product, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("Price: ${product.formattedPrice}", style = MaterialTheme.typography.bodyMedium)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Details"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    viewModel: ListDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Product Details") }) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                ProductDetailState.Loading, ProductDetailState.Initial -> {
                    CircularProgressIndicator()
                }
                is ProductDetailState.Error -> {
                    Text("Error: ${currentState.msg}")
                }
                is ProductDetailState.Success -> {
                    ProductDetailsContent(product = currentState.product)
                }
            }
        }
    }
}

@Composable
fun ProductDetailsContent(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder for Image
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Text("Image", modifier = Modifier.align(Alignment.Center))
        }
        Spacer(Modifier.height(16.dp))

        Text(product.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        Text(
            "Price: ${product.formattedPrice}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))

        Text(
            "Description:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            product.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 4.dp)
        )
    }
}

// ====================================================================================
// DEPENDENCY INJECTION (Hilt Module)
// ====================================================================================

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Binds the concrete implementation to the interface
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
}





/**

sealed class ProductListState {
    object Initial: ProductListState()
    object Loading: ProductListState()
    data class Success(val products: List<Product>): ProductListState()
    data class Error(val msg: String): ProductListState()
}

sealed class ProductDetailState {
    object Initial: ProductDetailState()
    object Loading: ProductDetailState()
    data class Success(val products: Product): ProductDetailState()
    data class Error(val msg: String): ProductDetailState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    fun onNavigateToListScreen(onNavigate: () -> Unit) {
        onNavigate()
    }
}

@HiltViewModel
class ListViewModel @Inject constructor(val getProductListUseCase: GetProductListUseCase): ViewModel() {
    private val _state = MutableStateFlow<ProductListState>(ProductListState.Initial)
    val state: StateFlow<ProductListState> = _state

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = ProductListState.Loading

            val result = getProductListUseCase()
            result.fold(
                onSuccess =  { products ->
                    _state.value = ProductListState.Success(products)
                },
                onFailure = { e ->
                    _state.value = ProductListState.Error("Failed to load products: ${e.message}")
                }
            )
        }
    }

    fun onProductSelected(id: String, onNavigate: (String)-> Unit) {
        onNavigate(id)
    }
}

@HiltViewModel
class ListDetailViewModel @Inject constructor(
    val getProductDetailsUseCase: GetProductDetailsUseCase
): ViewModel() {
    private val _state = MutableStateFlow<ProductDetailState>(ProductDetailState.Initial)
    val state: StateFlow<ProductDetailState> = _state

    init {

    }

    fun loadProductDetails(id: String) {
        viewModelScope.launch {
            val result = getProductDetailsUseCase()
            result.fold(
                onSuccess = { product ->
                    _state.value = ProductDetailState.Success(product)
                },
                onFailure = { e ->
                    _state.value = ProductDetailState.Error("Failed to load details: ${e.message}")
                }
            )
        }
    }

    // In com.app.presentation.home

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(
        viewModel: HomeViewModel = hiltViewModel(),
        onNavigateToList: () -> Unit
    ) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("App Home") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome to the Product App!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.onNavigateToListScreen(onNavigateToList) }
                ) {
                    Text("View Products List")
                }
            }
        }
    }
}

// In com.app.presentation.list

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    onNavigateToDetail: (productId: String) -> Unit
) {
    // Collect the StateFlow as Compose State
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Products") }) }
    ) { padding ->
        when (val currentState = state) {
            ProductListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ProductListState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${currentState.msg}")
                }
            }
            is ProductListState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(currentState.products, key = { it.id }) { product ->
                        ProductListItem(
                            product = product,
                            onClick = {
                                viewModel.onProductSelected(product.id, onNavigateToDetail)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun ProductListItem(product: Product, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("Price: ${product.price}", style = MaterialTheme.typography.bodyMedium)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Details"
        )
    }
}


// domain
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String = "",
    val description: String = ""
)


class GetProductListUseCase @Inject constructor() {
    suspend operator fun invoke(): Result<List<Product>> {

    }
}

class GetProductDetailsUseCase @Inject constructor() {
    suspend operator fun invoke(): Result<Product> {

    }
}




// data


*/
