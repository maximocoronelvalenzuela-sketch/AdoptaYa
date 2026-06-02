package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Notificacion

class NotificacionServicio {
    fun obtenerNotificaciones(): List<Notificacion> {
        return listOf(
            Notificacion(
                id = "1",
                titulo = "¡Alguien se interesó!",
                mensaje = "A Juan Perez le gustó tu mascota Max.",
                leida = false,
                idUsuario = "1",
                tipo = "FAVORITO",
                idMascota = "1", // Max
                idUsuarioEmisor = "2"
            ),
            Notificacion(
                id = "2",
                titulo = "Nueva mascota cerca",
                mensaje = "Se ha publicado a Luna cerca de tu ubicación.",
                leida = true,
                idUsuario = "1",
                tipo = "NUEVA_MASCOTA",
                idMascota = "2", // Luna
                idUsuarioEmisor = "2"
            ),
            Notificacion(
                id = "3",
                titulo = "Solicitud de contacto",
                mensaje = "Maria quiere contactarte por Max.",
                leida = false,
                idUsuario = "1",
                tipo = "CONTACTO",
                idMascota = "1", // Max
                idUsuarioEmisor = "2"
            )
        )
    }

    fun obtenerNotificacionesPorUsuario(idUsuario: String): List<Notificacion> {
        return obtenerNotificaciones().filter { it.idUsuario == idUsuario }
    }
}