package com.example.adoptaya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notificaciones")
data class Notificacion(
    @PrimaryKey val id: String,
    val titulo: String,
    val descripcion: String,
    val imagen: String?,
    val tipo: Enums.TipoNotificacion, // "FAVORITO", "NUEVA_MASCOTA", "CONTACTO". Requiere Converter
    val leida: Boolean,
    val fechaHora: String,
    val idUsuario: String,
    val idMascota: String? = null,
    val idUsuarioEmisor: String
)