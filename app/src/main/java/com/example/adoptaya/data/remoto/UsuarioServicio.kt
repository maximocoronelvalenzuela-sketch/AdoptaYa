package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Usuario

class UsuarioServicio {

    fun obtenerUsuarios(): List<Usuario> {
        return listOf(
            Usuario (
                id = "1",
                email = "juan@gmail.com",
                password = "123",
                nombre = "Juan",
                telefono = "123456789",
                latitud = -27.7834,
                longitud = -64.2642,
                provincia = "Santiago del Estero",
                ciudad = "Santiago del Estero",
                barrio = "Centro",
                imagen = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde",
                fechaHoraAlta = "2023-01-01T10:00:00"
            ),
            Usuario(
                id = "2",
                email = "maria@gmail.com",
                password = "123",
                nombre = "Maria",
                telefono = "987654321",
                latitud = -27.7850,
                longitud = -64.2660,
                provincia = "Santiago del Estero",
                ciudad = "Santiago del Estero",
                barrio = "Belgrano",
                imagen = "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
                fechaHoraAlta = "2023-02-15T11:30:00"
            )
        )
    }

    fun obtenerUsuarioPorId(id: String): Usuario? {
        return obtenerUsuarios().find { it.id == id }
    }
}