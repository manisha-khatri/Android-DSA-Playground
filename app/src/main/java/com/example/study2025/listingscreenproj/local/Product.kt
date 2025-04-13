package com.example.study2025.listingscreenproj

@Entity(tableName = "product")
data class Product(
    val id: Int,
    val name: String,
    val category: String
)
