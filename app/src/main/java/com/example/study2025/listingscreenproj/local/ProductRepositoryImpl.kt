package com.example.study2025.listingscreenproj.local

class ProductRepository(private val productDao: ProductDao) {

    suspend fun getAllProducts(): List<Product> = productDao.getAllProducts()

    suspend fun getProductsByCategory(category: String): List<Product> =
        productDao.getProductsByCategory(category)

    suspend fun seedInitialData() {
        val dummyProducts = listOf(
            Product(1, "Apple", "Fresh and juicy", "Fruits"),
            Product(2, "Banana", "Rich in potassium", "Fruits"),
            Product(3, "Carrot", "Good for eyes", "Vegetables"),
            Product(4, "Broccoli", "Packed with vitamins", "Vegetables")
        )
        productDao.insertAll(dummyProducts)
    }
}
