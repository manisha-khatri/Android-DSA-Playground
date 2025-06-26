package com.example.study2025.recyclerview.viewtypes

sealed class ListItem {
    object Header: ListItem()
    data class TextItem(val text: String): ListItem()
    data class ImageItem(val imgId: Int): ListItem()
}