package com.example.study2025.compose.quoteapp.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.study2025.compose.quoteapp.model.Quote

@Composable
fun QuoteList(data: Array<Quote>, onClick: (quote: Quote) -> Unit) {
    LazyColumn {
        items(data) { quote ->
            QuoteListItem(quote = quote, onClick = onClick)
        }
    }

}