package com.example.study2025.listingscreenproj.local

interface ProductRepository {
    suspend fun seedInitialData()
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductsByCategory(category: String): List<Product>
}
