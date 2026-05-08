package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.data.remoto.NotificacionServicio
import com.example.adoptaya.dominio.repositorio.NotificacionRepositorio

class NotificacionRepositorioImpl (
    private val servicio: NotificacionServicio
) : NotificacionRepositorio {

    override fun obtenerNotificaciones(): List<Notificacion> {
        return servicio.obtenerNotificaciones()
    }

    override fun obtenerNotificacionesPorUsuario(idUsuario: String): List<Notificacion> {
        return servicio.obtenerNotificacionesPorUsuario(idUsuario)
    }
}