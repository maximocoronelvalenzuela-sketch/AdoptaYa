package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Notificacion

class NotificacionServicio {
    fun obtenerNotificaciones(): List<Notificacion> {
        return listOf(
            Notificacion(
                id = "1",
                titulo = "Nueva mascota en adopción",
                mensaje = "Una nueva mascota en adopción ha sido publicada",
                idUsuario = "1"
            ),
            Notificacion(
                id = "2",
                titulo = "Recordatorio",
                mensaje = "No olvides completar tu perfil",
                idUsuario = "1"
            ),
            Notificacion(
                id = "3",
                titulo = "Nueva mascota en adopción",
                mensaje = "Una nueva mascota en adopción ha sido publicada",
                idUsuario = "2"
            )
        )
    }

    fun obtenerNotificacionesPorUsuario(idUsuario: String): List<Notificacion> {
        return obtenerNotificaciones().filter { it.idUsuario == idUsuario }
    }
}