package com.example.adoptaya.data.model

data class Usuario(
    val id: String,
    val email: String,
    val contraseña: String,
    val nombre: String,
    val telefono: String,
    val latitud: Double,
    val longitud: Double,
    val provincia: String,
    val ciudad: String,
    val barrio: String,
    val imagen: String,
    val fechaHoraAlta: String
)
