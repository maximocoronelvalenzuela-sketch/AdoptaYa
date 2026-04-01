package com.example.adoptaya.data.model

data class Mascota(
    val id: String,
    val nombre: String,
    val tipo: String,
    val edad: String,
    val genero: String,
    val esterilizado: Boolean,
    val vacunado: Boolean,
    val desparasitado: Boolean,
    val descripcionPersonalidad: String,
    val descripcionAdicional: String,
    val estado: String,
    val latitud: Double,
    val longitud: Double,
    val imagenes: List<String>,
    val idUsuario: String,
    val fechaHoraAlta: String
)