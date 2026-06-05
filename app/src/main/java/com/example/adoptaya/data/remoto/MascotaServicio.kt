package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota

class MascotaServicio {
    fun obtenerMascotas(): List<Mascota> {
        return listOf(
            Mascota(
                id = "1",
                nombre = "Max",
                tipo = "Perro",
                edad = "3",
                sexo = "Macho",
                raza = "Beagle",
                tamaño = "Mediano",
                esterilizado = true,
                vacunado = true,
                desparasitado = true,
                descripcionPersonalidad = listOf("Activo", "Sociable", "Cariñoso"),
                descripcionAdicional = "Ideal para familias",
                estado = Enums.EstadoMascota.DISPONIBLE,
                latitud = -27.80799,
                longitud = -64.24882,
                provincia = "Santiago del Estero",
                ciudad = "Santiago del Estero",
                barrio = "Centro",
                imagenes = listOf("https://images.unsplash.com/photo-1543466835-00a7907e9de1"),
                idUsuario = "usuario123",
                fechaHoraAlta = "2023-07-15T12:00:00"
            ),
            Mascota(
                id = "2",
                nombre = "Luna",
                tipo = "Gato",
                edad = "1",
                sexo = "Hembra",
                tamaño = "Chico",
                esterilizado = true,
                vacunado = true,
                desparasitado = true,
                descripcionPersonalidad = listOf("Tranquilo", "Cariñoso", "Amigable"),
                descripcionAdicional = "Busca un hogar sin otros gatos",
                estado = Enums.EstadoMascota.DISPONIBLE,
                latitud = -27.80922,
                longitud = -64.24835,
                provincia = "Santiago del Estero",
                ciudad = "Santiago del Estero",
                barrio = "Belgrano",
                imagenes = listOf("https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba"),
                idUsuario = "usuario456",
                fechaHoraAlta = "2024-01-10T10:30:00"
            )
        )
    }

    fun obtenerMascotaPorId(id: String): Mascota? {
        return obtenerMascotas().find { it.id == id }
    }

    fun filtrarPorTipo(tipo: String): List<Mascota> {
        return obtenerMascotas().filter { it.tipo == tipo }
    }

    fun filtrarPorSexo(sexo: String): List<Mascota> {
        return obtenerMascotas().filter { it.sexo == sexo }
    }
}