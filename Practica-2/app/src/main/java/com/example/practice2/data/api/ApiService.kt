package com.example.practice2.data.api

import com.example.practice2.model.LoginRequest
import com.example.practice2.model.RegistroUsuarioDTO
import com.example.practice2.model.Usuario
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<String>


    @POST("auth/register")
    fun register(
        @Body request: RegistroUsuarioDTO,
        @Query("role") role: String = "USER"
    ): Call<Usuario>

    @GET("usuarios/email/{email}")
    fun obtenerUsuarioPorEmail(
        @Path("email") email: String,
        @Header("Authorization") token: String
    ): Call<Usuario>

    @Multipart
    @PUT("usuarios/foto/{email}")
    fun actualizarFotoPerfil(
        @Path("email") email: String,
        @Part foto: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @PUT("usuarios/actualizar/{emailOriginal}")
    fun actualizarNombreCorreo(
        @Path("emailOriginal") emailOriginal: String,
        @Query("nombre") nombre: String,
        @Query("emailNuevo") emailNuevo: String,
        @Header("Authorization") token: String
    ): Call<Usuario>

    @DELETE("admin/usuario/{id}")
    fun eliminarUsuario(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Call<Void>

    @GET("admin/usuarios")
    fun obtenerTodosLosUsuarios(
        @Header("Authorization") token: String
    ): Call<List<Usuario>>



}
