package com.example.adoptaya.dominio.usecase.notificacion

import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.dominio.repositorio.NotificacionRepositorio

class ObtenerNotificacionesPorUsuario (
    private val repositorio: NotificacionRepositorio
) {

    operator suspend fun invoke(idUsuario: String): List<Notificacion> {
        return repositorio.obtenerNotificacionesPorUsuario(idUsuario)
    }
}