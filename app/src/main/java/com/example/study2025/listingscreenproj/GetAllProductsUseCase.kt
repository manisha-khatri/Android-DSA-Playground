package com.example.study2025.listingscreenproj

import com.example.study2025.listingscreenproj.local.Product
import com.example.study2025.listingscreenproj.local.ProductRepository

class GetAllProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(): List<Product> = repository.getAllProducts()
}
