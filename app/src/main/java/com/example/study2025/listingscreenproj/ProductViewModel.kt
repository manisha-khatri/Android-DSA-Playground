package com.example.study2025.listingscreenproj

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.study2025.listingscreenproj.local.Product
import com.example.study2025.listingscreenproj.local.ProductDatabase
import com.example.study2025.listingscreenproj.local.ProductRepository
import com.example.study2025.listingscreenproj.local.ProductRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ProductDatabase.getInstance(application).productDao()
    private val repository: ProductRepository = ProductRepositoryImpl(dao)

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private var allProductsCache: List<Product>? = null

    fun seedData() {
        viewModelScope.launch {
            repository.seedInitialData()
            loadAllProducts()
        }
    }

    fun loadAllProducts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            if (allProductsCache != null) {
                _uiState.value = if (allProductsCache!!.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(allProductsCache!!)
                }
            } else {
                try {
                    val products = repository.getAllProducts()
                    allProductsCache = products
                    _uiState.value = if (products.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(products)
                    }
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.localizedMessage ?: "Something went wrong")
                }
            }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val products = repository.getProductsByCategory(category)
                _uiState.value = if (products.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(products)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Error loading category")
            }
        }
    }
}

