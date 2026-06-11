package com.example.adoptaya.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascotas")
data class Mascota(
    @PrimaryKey val id: String,
    val nombre: String,
    val tipo: Enums.TipoMascota,
    val edad: String,
    val tiempo: Enums.TiempoEdad,
    val sexo: Enums.SexoMascota,
    val raza: String? = null,
    val tamaño: Enums.TamañoMascota,
    val esterilizado: Boolean,
    val vacunado: Boolean,
    val desparasitado: Boolean,
    val descripcionPersonalidad: List<String>,  // Requiere Converter
    val descripcionAdicional: String,
    val estado: Enums.EstadoMascota,            // Requiere Converter
    val latitud: Double,
    val longitud: Double,
    val provincia: String?,
    val ciudad: String?,
    val barrio: String?,
    val imagenes: List<String>, // Lista de links. Requiere Converter
    val fechaHoraAlta: String,
    val idUsuario: String
)