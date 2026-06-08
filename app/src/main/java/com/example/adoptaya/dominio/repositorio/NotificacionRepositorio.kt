package com.example.adoptaya.dominio.repositorio

import com.example.adoptaya.data.model.Notificacion

interface NotificacionRepositorio {
    suspend fun obtenerNotificacionesPorUsuario(idUsuario: String): List<Notificacion>

    suspend fun guardarNotificacion(notificacion: Notificacion)
}