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
                latitud = -27.80799,
                longitud = -64.24882,
                imagenes = listOf("imagen1.jpg", "imagen2.jpg"),
                idUsuario = "usuario123",
                fechaHoraAlta = "2023-07-15T12:00:00"
            ),
            Mascota(
                id = "2",
                nombre = "Luna",
                tipo = "Gato",
                edad = "1 año",
                genero = "Hembra",
                esterilizado = true,
                vacunado = true,
                desparasitado = true,
                descripcionPersonalidad = "Tranquila y mimosa",
                descripcionAdicional = "Busca un hogar sin otros gatos",
                estado = "Disponible",
                latitud = -27.80922,
                longitud = -64.24835,
                imagenes = listOf("luna1.jpg"),
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

    fun filtrarPorGenero(genero: String): List<Mascota> {
        return obtenerMascotas().filter { it.genero == genero }
    }
}