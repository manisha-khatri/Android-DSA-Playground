package com.example.systemdesign.interview.tesco.mock
/*

import android.content.Context
import com.example.systemdesign.interview.tesco.CategorizedProductResponse
import com.example.systemdesign.interview.tesco.SearchApiService
import com.example.systemdesign.interview.tesco.SearchSuggestion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class JsonAssetReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun readJson(fileName: String): String {
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}

class MockSearchApiService @Inject constructor(
    private val jsonReader: JsonAssetReader
) : SearchApiService {
    private val gson = Gson()

    override suspend fun fetchSuggestions(query: String): List<SearchSuggestion> {
        val json = jsonReader.readJson("suggestions.json")
        val all = gson.fromJson<List<SearchSuggestion>>(
            json,
            object : TypeToken<List<SearchSuggestion>>() {}.type
        )

        return all.filter {
            it.text.contains(query, ignoreCase = true)
        }
    }

    override suspend fun fetchProducts(query: String): CategorizedProductResponse {
        val fileName = when (query.lowercase()) {
            "apple" -> "products_apple.json"
            else -> "products_apple.json" // fallback
        }

        val json = jsonReader.readJson(fileName)
        return gson.fromJson(json, CategorizedProductResponse::class.java)
    }
}*/
