package com.example.adoptaya.dominio.repositorio

import com.example.adoptaya.data.model.Notificacion

interface NotificacionRepositorio {
    fun obtenerNotificaciones(): List<Notificacion>

    fun obtenerNotificacionesPorUsuario(idUsuario: String): List<Notificacion>
}