package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.local.dao.NotificacionDao
import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.dominio.repositorio.NotificacionRepositorio

class NotificacionRepositorioImpl(
    private val notificacionDao: NotificacionDao
) : NotificacionRepositorio {

    override suspend fun obtenerNotificacionesPorUsuario(idUsuario: String): List<Notificacion> {
        return notificacionDao.obtenerPorUsuario(idUsuario)
    }

    override suspend fun guardarNotificacion(notificacion: Notificacion) {
        notificacionDao.insertarNotificacion(notificacion)
    }
}