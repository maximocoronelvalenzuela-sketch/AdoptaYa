package com.example.adoptaya.data.model

data class Notificacion(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val imagen: String?,
    val tipo: Enums.TipoNotificacion, // "FAVORITO", "NUEVA_MASCOTA", "CONTACTO"
    val leida: Boolean,
    val fechaHora: String,
    val idUsuario: String,
    val idMascota: String? = null,
    val idUsuarioEmisor: String
)