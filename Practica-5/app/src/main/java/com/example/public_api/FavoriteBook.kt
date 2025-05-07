// models/FavoriteBook.kt
package com.example.public_api.models

data class FavoriteBook(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val year: Int? = null
)
