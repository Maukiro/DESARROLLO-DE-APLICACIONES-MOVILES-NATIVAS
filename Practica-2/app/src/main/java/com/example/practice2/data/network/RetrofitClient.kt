package com.example.practice2.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory // ← ✅ IMPORTANTE

object RetrofitClient {

    private const val BASE_URL = "http://192.168.100.16:8080/api/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // ✅ Este debe ir primero
            .addConverterFactory(ScalarsConverterFactory.create())
            // ✅ Luego el de JSON
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
