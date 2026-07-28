package com.example.adoptaya.dominio.usecase.notificacion

import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.dominio.repositorio.NotificacionRepositorio

class GuardarNotificacionUseCase(
    private val notificacionRepositorio: NotificacionRepositorio
) {
    suspend operator fun invoke(notificacion: Notificacion) {
        notificacionRepositorio.guardarNotificacion(notificacion)
    }
}
