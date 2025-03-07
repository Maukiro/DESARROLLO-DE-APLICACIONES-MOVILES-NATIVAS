package com.example.basicbackend

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.16:8080/" // Para emulador

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // URL base del backend
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a objetos Kotlin
            .build()

        retrofit.create(ApiService::class.java) // Crea la implementación de ApiService
    }
}
