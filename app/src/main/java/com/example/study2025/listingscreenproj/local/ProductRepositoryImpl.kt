package com.example.study2025.listingscreenproj.local

class ProductRepositoryImpl(private val productDao: ProductDao) : ProductRepository {

    override suspend fun seedInitialData() {
        val products = listOf(
            Product(1, "Apple", "Fresh and juicy", "Fruits"),
            Product(2, "Banana", "Rich in potassium", "Fruits"),
            Product(3, "Carrot", "Good for eyes", "Vegetables"),
            Product(4, "Broccoli", "Packed with vitamins", "Vegetables")
        )
        productDao.insertAll(products)
    }

    override suspend fun getAllProducts(): List<Product> {
        return productDao.getAllProducts()
    }

    override suspend fun getProductsByCategory(category: String): List<Product> {
        return productDao.getProductsByCategory(category)
    }
}
