package com.example.study2025.systemdesign.interview

/**


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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

sealed class ProductListState {
    object Initial: ProductListState()
    object Loading: ProductListState()
    data class Success(val products: List<Product>): ProductListState()
    data class Error(val msg: String): ProductListState()
}

sealed class ProductDetailState {
    object Initial: ProductDetailState()
    object Loading: ProductDetailState()
    data class Success(val products: List<Product>): ProductDetailState()
    data class Error(val msg: String): ProductDetailState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    fun onNavigateToListScreen(onNavigate: () -> Unit) {
        onNavigate()
    }
}

@HiltViewModel
class ListViewModel @Inject constructor(
    val productUseCase:
): ViewModel() {

}







// domain
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String = "",
    val description: String = ""
)





// data




*/




