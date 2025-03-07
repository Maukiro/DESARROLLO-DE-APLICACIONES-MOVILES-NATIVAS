package com.example.basicbackend

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/mensaje")
    fun obtenerMensaje(): Call<Mensaje> // Retrofit manejará la conversión de JSON a la clase Mensaje
}
