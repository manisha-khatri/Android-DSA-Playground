package com.example.study2025.listingscreenproj

import com.example.study2025.listingscreenproj.local.Product

sealed class UiState {
    object Loading : UiState()
    object Empty : UiState()
    data class Success(val products: List<Product>) : UiState()
    data class Error(val message: String) : UiState()
}
