package com.example.public_api.network

import com.example.public_api.models.Book
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("search.json")
    fun searchBooks(@Query("q") query: String): Call<SearchResponse>
}

data class SearchResponse(val docs: List<Book>)