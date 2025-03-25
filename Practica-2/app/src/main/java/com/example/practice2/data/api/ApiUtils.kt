package com.example.practice2.data.api

import com.example.practice2.data.network.RetrofitClient

object ApiUtils {
    val apiService: ApiService = RetrofitClient.instance.create(ApiService::class.java)
}
