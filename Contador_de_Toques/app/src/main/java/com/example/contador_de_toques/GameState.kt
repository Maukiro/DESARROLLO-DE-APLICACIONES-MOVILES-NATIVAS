package com.example.contador_de_toques

data class GameState(
    val score: Int,
    val level: Int,
    val timeElapsed: Long,
    val theme: String,
    val format: String
)
