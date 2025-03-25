package com.example.practice2.model


data class Usuario(
    val id: Long,
    val nombre: String,
    val email: String,
    val role: String,
    val fotoPerfil: String? // ✅ Nuevo campo
)

