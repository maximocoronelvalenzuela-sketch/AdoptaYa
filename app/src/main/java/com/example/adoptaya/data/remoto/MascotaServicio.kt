package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Mascota

class MascotaServicio {
    fun obtenerMascotas(): List<Mascota> {
        return listOf(
            Mascota(
                id = "1",
                nombre = "Max",
                tipo = "Perro",
                edad = "3 años",
                genero = "Macho",
                esterilizado = true,
                vacunado = true,
                desparasitado = true,
                descripcionPersonalidad = "Activo y sociable",
                descripcionAdicional = "Ideal para familias",
                estado = "Disponible",
                latitud = 40.7128,
                longitud = -74.0060,
                imagenes = listOf("imagen1.jpg", "imagen2.jpg"),
                idUsuario = "usuario123",
                fechaHoraAlta = "2023-07-15T12:00:00"
            )
        )
    }
}