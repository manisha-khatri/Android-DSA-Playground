
Design a simple E-Commerce Android app with multiple screens.
- The app should:
    - Show a list of products
    - Allow viewing product details
    - Allow adding/removing items from cart
    - Sync data with backend (mocked)
    - Follow clean, scalable architecture
    - Handle state changes correctly

Later :
Search feature
---------------------------------------------------
- HomePage
    - Product Search: type + enter (API)
    - Show a list of products --> Pagination (API)
    - Add/remove product --> cached
- ProductDetailScreen
    - Get Product Details --> (API)
    - Add/remove product --> cached
- CartScreen
    - Add/remove product --> cached
    - Order place --> (API)

- caching
- pagination
---------------------------------------------------

Logic to navigate from 1 screen to another:

@Composable
fun HomePageScreen(
    viewModel: HomePageVM = hiltViewModel(),
    onProductNavigate: (String) -> Unit,
    onCheckoutNavigate: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onProductNavigate("productId") }
    ) {
        Text("Product Navigation")
    }

    Column(
            modifier = Modifier.clickable { onCheckoutNavigate() }
        ) {
            Text("Checkout Navigation")
        }
}

object Routes {
    const val HOME_PAGE = "HOME_PAGE"
    const val PRODUCT_DETAIL_PAGE = "PRODUCT_DETAIL_PAGE"
    const val CHECKOUT_PAGE = "CHECKOUT_PAGE"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController=navController, startDestination=Routes.HOME_PAGE) {
        composable(route=Routes.HOME_PAGE) {
            HomePageScreen(
                onProductNavigate = { id ->
                    navController.navigate("${Routes.HOME_PAGE}/$id")
                },
                onCartNavigate = {
                    navController.navigate(Routes.CHECKOUT_PAGE)
                }
            )
        }

        composable(
            routes=Routes.PRODUCT_DETAIL_PAGE,
            arguments = listOf(navArgument("id") { type = NavType.StringType})
        ) {
            ProductDetailScreen()
        }

        composable(routes=Routes.CHECKOUT_PAGE) {
            CheckoutScreen()
        }
    }
}

@HiltViewModel
class ProductDetailVM: ViewModel @Inject constructor(
    val repo: ProductRepo,
    savedStateHandle: SavedStateHandle
) {
    init {
        val productId: String = savedStateHandle["productId"]
        if(productId!=null) {
            fetchProducts(productId)
        }
    }
}



