package com.example.study2025.compose.quoteapp

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.example.study2025.compose.quoteapp.model.Quote
import com.google.gson.Gson

object DataManager {

    var data = emptyArray<Quote>()
    var isDataLoaded = mutableStateOf(false)
    var currentPage = mutableStateOf(Pages.LISTING)
    var currentQuote: Quote? = null

    fun loadAssetsFromFile(context: Context) {
        try {
            val inputStream = context.assets.open("quotes.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val json = String(buffer, Charsets.UTF_8)
            val gson = Gson()

            data = gson.fromJson(json, Array<Quote>::class.java)
            isDataLoaded.value = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun switchPages(quote: Quote?) {
        // If on the Listing page then go to the detail page
        if(currentPage.value == Pages.LISTING) {
            currentQuote = quote
            currentPage.value = Pages.DETAIL
        } else {
            currentPage.value = Pages.LISTING
        }
    }
}