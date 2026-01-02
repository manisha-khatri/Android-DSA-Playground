package com.example.systemdesign.prep.phonepe

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.SavedStateHandle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.PrimaryKey
import androidx.room.Room
import com.example.systemdesign.BuildConfig
import com.example.systemdesign.prep.phonepe.mock.JsonAssetReader
import com.example.systemdesign.prep.phonepe.mock.MockProductApiService
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 *
---------------------------------------------------
- HomePage
    - Show a list of products --> Pagination (API)
    - Add/remove product --> cached
- ProductDetailScreen
    - Get Product Details --> (API)
    - Add/remove product --> cached
- CartScreen
    - Add/remove product --> cached
    - Order place --> (API)
---------------------------------
/ui
    - HomePage
    - HomeViewModel
    - HomePageUiState

    - ProductDetailScreen
    - ProductDetailViewModel
    - ProductDetailUiState

    - CartScreen
    - CartViewModel
    - CartUiState

    - Navigation

/domain


/data
    /remote
        - ApiService
    /local
        - CheckOutDataDao
    /repository
        - ProductRepository

Future Scope:-
- Checkout page: show successful toast, update the screen with successful message
- SearchBar: search product
- cursor navigation
---------------------------------------------------
 */

//--------------------------DATA-------------------------
interface ApiService {
    @GET("homepage")
    suspend fun getHomePageData(): HomePageData

    @GET("product")
    suspend fun fetchProductDetails(@retrofit2.http.Query("id") productId: String) : Product

    @POST("checkout")
    suspend fun createCheckout(@Body products: List<Product>)
}

@Dao
interface CartDao {
    @Query("SELECT * FROM product")
    fun getAllCartProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductToCart(product: Product)

    @Delete
    suspend fun deleteProductFromCart(product: Product)
}

@Database(
    entities = [Product::class],
    version = 1
)
abstract class CartDatabase: RoomDatabase() {
    abstract fun getDao(): CartDao
}

class ProductRepositoryImpl @Inject constructor(
    val dao: CartDao,
    val api: ApiService
): ProductRepository {
    override fun getAllCartProducts(): Flow<List<Product>> {
        return dao.getAllCartProducts()
    }

    override suspend fun addToCart(product: Product) {
        dao.insertProductToCart(product)
    }

    override suspend fun deleteFromCart(product: Product) {
        dao.deleteProductFromCart(product)
    }

    override suspend fun getHomePageData(): HomePageData {
        return api.getHomePageData()
    }

    override suspend fun fetchProductDetails(productId: String) : Product {
        return api.fetchProductDetails(productId)
    }

    override suspend fun createCheckout(products: List<Product>) {
        return api.createCheckout(products)
    }
}

//--------------------------Domain-------------------------

interface ProductRepository {
    fun getAllCartProducts(): Flow<List<Product>>

    suspend fun addToCart(product: Product)

    suspend fun deleteFromCart(product: Product)

    suspend fun getHomePageData(): HomePageData

    suspend fun fetchProductDetails(productId: String) : Product

    suspend fun createCheckout(products: List<Product>)
}

data class HomePageData(
    val banner: Banner, // promotional banner
    val recommendedProductsTitle: String,
    val recommendedProducts: List<ProductPreview>,
    val productListTitle: String,
    val productList: List<ProductPreview>
)

data class Banner(
    val id: String,
    val image: String,
    val action: String
)

data class ProductPreview(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val availableQuantity: Int
)

@Entity(tableName = "product")
data class Product( // ProductDetails
    @PrimaryKey
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val offer: String,
    val rating: Double,
    val availableQuantity: Int
)

//--------------------------UI-------------------------
sealed interface HomePageUiState {
    object Loading: HomePageUiState
    data class Success(val data: HomePageData): HomePageUiState
    class Error(val msg: String): HomePageUiState
}

@HiltViewModel
class HomePageViewModel @Inject constructor(val repository: ProductRepository): ViewModel() {
    private val _uiState = MutableStateFlow<HomePageUiState>(HomePageUiState.Loading)
    val uiState: StateFlow<HomePageUiState> = _uiState.asStateFlow()

    init {
        fetchHomePageData()
    }

    fun fetchHomePageData() {
        viewModelScope.launch {
            _uiState.value = HomePageUiState.Loading
            try {
                val data = repository.getHomePageData()
                _uiState.value = HomePageUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = HomePageUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}

sealed interface ProductUiState {
    object Loading: ProductUiState
    data class Success(val product: Product): ProductUiState
    class Error(val msg: String): ProductUiState
}

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val productId: String? = savedStateHandle["productId"]
        if(productId != null) {
            fetchProductDetails(productId)
        }
    }

    fun fetchProductDetails(id: String) {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            try {
                val data = repository.fetchProductDetails(id)
                _uiState.value = ProductUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(e.message ?: "Failed to load")
            }
        }
    }

    fun toggleCartStatus(product: Product, isAdded: Boolean) {
        viewModelScope.launch {
            try {
                if (isAdded) {
                    repository.addToCart(product)
                } else {
                    repository.deleteFromCart(product)
                }
                // Optionally update local UI state here to show "Added" immediately
            } catch (e: Exception) {
                // Handle cart error (e.g., via a Channel for one-time SnackBar events)
            }
        }
    }
}

sealed interface CartUiState {
    object Loading: CartUiState
    data class Success(val products: List<Product>): CartUiState
    class Error(val msg: String): CartUiState
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(val repository: ProductRepository): ViewModel() {
    val uiState: StateFlow<CartUiState> = repository.getAllCartProducts()
        .map { products ->
            CartUiState.Success(products) as CartUiState
        }
        .catch { e ->
            emit(CartUiState.Error(e.message.toString()))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState.Loading
        )

    fun onCheckoutClick(products: List<Product>) {
        viewModelScope.launch {
            try {
                repository.createCheckout(products)
            } catch (e: Exception) {

            }
        }
    }
}


// navigation
object Routes {
    const val HOME_PAGE = "HOME_PAGE"
    const val PRODUCT_DETAIL_PAGE = "PRODUCT_DETAIL_PAGE"
    const val CART_PAGE = "CART_PAGE"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME_PAGE
    ) {
        composable(Routes.HOME_PAGE) {
            HomePageScreen(
                onProductNavigate = { productId ->
                    navController.navigate("${Routes.PRODUCT_DETAIL_PAGE}/$productId")
                },
                onCheckoutNavigate = {
                    navController.navigate(Routes.CART_PAGE)
                }
            )
        }

        composable(
            route = "${Routes.PRODUCT_DETAIL_PAGE}/{productId}",
            arguments = listOf(navArgument("productId") {type = NavType.StringType})
        ) {
            ProductDetailScreen()
        }

        composable(Routes.CART_PAGE) {
            CheckOutScreen()
        }
    }
}

/**
- HomePage
- ProductDetailScreen
- CartScreen
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScaffold()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute == Routes.HOME_PAGE) {
                TopAppBar(title = { Text("Home Page") })
            } else if (currentRoute == Routes.PRODUCT_DETAIL_PAGE) {
                TopAppBar(title = { Text("Product Detail") })
            } else if (currentRoute == Routes.CART_PAGE) {
                TopAppBar(title = { Text("Checkout Page") })
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AppNavGraph(navController)
        }
    }
}


@HiltAndroidApp
class EcommerceApp: Application()

//--------------------------DI-------------------------
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockProductApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealProductApi

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ---------------- DATABASE ----------------
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ): CartDatabase {
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            "cart_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesCartDao(db: CartDatabase): CartDao =
        db.getDao()

    // ---------------- REAL API ----------------

    @Provides
    @Singleton
    @RealProductApi
    fun provideRealApiService(): ApiService =
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    // ---------------- MOCK API ----------------

    @Provides
    @Singleton
    @MockProductApi
    fun provideMockApiService(
        jsonAssetReader: JsonAssetReader
    ): ApiService =
        MockProductApiService(jsonAssetReader)

    @Provides
    @Singleton
    fun provideJsonAssetReader(
        @ApplicationContext context: Context
    ): JsonAssetReader {
        return JsonAssetReader(context)
    }

    // ---------------- FINAL API (USED EVERYWHERE) ----------------

    @Provides
    @Singleton
    fun provideApiService(
        @MockProductApi mockApi: ApiService,
        @RealProductApi realApi: ApiService
    ): ApiService {
        return if (BuildConfig.USE_MOCK_API) mockApi else realApi
    }

    // ---------------- REPOSITORY ----------------

    @Provides
    @Singleton
    fun providesRepository(
        api: ApiService,
        dao: CartDao
    ): ProductRepository =
        ProductRepositoryImpl(dao, api)
}
