package com.example.systemdesign.prep.phonepe.mock

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import com.example.systemdesign.prep.phonepe.ApiService
import com.example.systemdesign.prep.phonepe.HomePageData
import com.example.systemdesign.prep.phonepe.Product

class JsonAssetReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun readJson(fileName: String): String {
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}

class MockProductApiService @Inject constructor(
    private val jsonAssetReader: JsonAssetReader
) : ApiService {

    private val gson = Gson()

    override suspend fun getHomePageData(): HomePageData {
        return try {
            val json = jsonAssetReader.readJson("homepage.json")
            Log.d("MockApi", "JSON loaded: $json")
            gson.fromJson(json, HomePageData::class.java)
        } catch (e: Exception) {
            Log.e("MockApi", "Failed to load homepage", e)
            throw e
        }
    }


    override suspend fun fetchProductDetails(productId: String): Product {
        val json = jsonAssetReader.readJson("products.json")
        val products: List<Product> =
            gson.fromJson(json, object : TypeToken<List<Product>>() {}.type)

        return products.firstOrNull { it.id == productId }
            ?: throw IllegalArgumentException("Product not found")
    }

    override suspend fun createCheckout(products: List<Product>) {
        delay(500)
        Log.d("MockApi", "Checkout success with ${products.size} products")
    }
}
