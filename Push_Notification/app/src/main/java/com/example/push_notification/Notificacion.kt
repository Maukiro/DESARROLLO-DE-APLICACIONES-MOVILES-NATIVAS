package com.example.push_notification

data class Notificacion(
    val titulo: String = "",
    val mensaje: String = "",
    val timestamp: Long = 0,
    val destinatario: String = ""
)
