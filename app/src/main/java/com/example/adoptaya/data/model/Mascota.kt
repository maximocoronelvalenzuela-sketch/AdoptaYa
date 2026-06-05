package com.example.adoptaya.data.model

data class Mascota(
    val id: String,
    val nombre: String,
    val tipo: String,
    val edad: String,
    val sexo: String,
    val raza: String? = null,
    val tamaño: String,
    val esterilizado: Boolean,
    val vacunado: Boolean,
    val desparasitado: Boolean,
    val descripcionPersonalidad: List<String>,
    val descripcionAdicional: String,
    val estado: Enums.EstadoMascota,
    val latitud: Double,
    val longitud: Double,
    val provincia: String,
    val ciudad: String,
    val barrio: String,
    val imagenes: List<String>, // Lista de links
    val fechaHoraAlta: String,
    val idUsuario: String
)