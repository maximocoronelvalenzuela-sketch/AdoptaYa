package com.example.adoptaya.data.model

data class Notificacion(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val leida: Boolean,
    val idUsuario: String,
    val tipo: String, // "FAVORITO", "NUEVA_MASCOTA", "CONTACTO"
    val idMascota: String? = null,
    val idUsuarioEmisor: String
)