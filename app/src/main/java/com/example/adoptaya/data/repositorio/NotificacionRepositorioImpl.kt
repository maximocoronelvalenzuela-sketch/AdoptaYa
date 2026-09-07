package com.example.adoptaya.data.repositorio

import android.util.Log
import com.example.adoptaya.data.local.dao.NotificacionDao
import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.data.remoto.RetrofitClient
import com.example.adoptaya.dominio.repositorio.NotificacionRepositorio

class NotificacionRepositorioImpl(
    private val notificacionDao: NotificacionDao
) : NotificacionRepositorio {

    private val api = RetrofitClient.apiService
    private val TAG = "API_ADOPTAYA_NOTIF"

    override suspend fun obtenerNotificacionesPorUsuario(idUsuario: String): List<Notificacion> {
        try {
            val response = api.obtenerNotificacionesPorUsuario(idUsuario)
            if (response.isSuccessful) {
                response.body()?.let { notifsRemotas ->
                    notifsRemotas.forEach { notificacionDao.insertarNotificacion(it) }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error de red en notificaciones, usando caché: ${e.message}")
        }
        return notificacionDao.obtenerPorUsuario(idUsuario)
    }

    override suspend fun guardarNotificacion(notificacion: Notificacion) {
        notificacionDao.insertarNotificacion(notificacion)
        try {
            api.guardarNotificacion(notificacion)
        } catch (e: Exception) {
            Log.e(TAG, "No se pudo sincronizar notificación: ${e.message}")
        }
    }
}