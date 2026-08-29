package com.example.adoptaya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val id: String,
    val email: String,
    val nombre: String,
    val telefono: String,
    val latitud: Double,
    val longitud: Double,
    val provincia: String,
    val ciudad: String,
    val barrio: String,
    val imagen: String?,
    val fechaHoraAlta: String
)
