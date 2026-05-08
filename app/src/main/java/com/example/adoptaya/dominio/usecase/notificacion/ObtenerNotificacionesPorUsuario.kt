package com.example.adoptaya.dominio.usecase.notificacion

import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.data.repositorio.NotificacionRepositorioImpl

class ObtenerNotificacionesPorUsuario (
    private val repositorio: NotificacionRepositorioImpl
) {

    operator fun invoke(idUsuario: String): List<Notificacion> {
        return repositorio.obtenerNotificacionesPorUsuario(idUsuario)
    }
}