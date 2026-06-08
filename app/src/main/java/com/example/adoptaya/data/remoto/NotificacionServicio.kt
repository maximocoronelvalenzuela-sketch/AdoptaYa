package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.data.model.TipoNotificacion

class NotificacionServicio {
    fun obtenerNotificaciones(): List<Notificacion> {
        return listOf(
            Notificacion(
                id = "1",
                titulo = "¡Alguien se interesó!",
                descripcion = "A Juan Perez le gustó tu mascota Max.",
                imagen = null,
                tipo = TipoNotificacion.FAVORITO,
                leida = false,
                fechaHora = "2023-07-15 12:00:00",
                idUsuario = "1",
                idMascota = "1", // Max
                idUsuarioEmisor = "2"
            ),
            Notificacion(
                id = "2",
                titulo = "Nueva mascota cerca",
                descripcion = "Se ha publicado a Luna cerca de tu ubicación.",
                imagen = null,
                tipo = TipoNotificacion.NUEVA_MASCOTA,
                leida = true,
                fechaHora = "2023-07-15 12:00:00",
                idUsuario = "1",
                idMascota = "2", // Luna
                idUsuarioEmisor = "2"
            ),
            Notificacion(
                id = "3",
                titulo = "Solicitud de contacto",
                descripcion = "Maria quiere contactarte por Max.",
                imagen = null,
                tipo = TipoNotificacion.CONTACTO,
                leida = false,
                fechaHora = "2023-07-15 12:00:00",
                idUsuario = "1",
                idMascota = "1", // Max
                idUsuarioEmisor = "2"
            )
        )
    }

    fun obtenerNotificacionesPorUsuario(idUsuario: String): List<Notificacion> {
        return obtenerNotificaciones().filter { it.idUsuario == idUsuario }
    }
}