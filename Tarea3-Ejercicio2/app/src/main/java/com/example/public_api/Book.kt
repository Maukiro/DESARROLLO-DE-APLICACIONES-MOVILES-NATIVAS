package com.example.public_api.models

data class Book(
    val title: String,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val cover_i: Int?
)
