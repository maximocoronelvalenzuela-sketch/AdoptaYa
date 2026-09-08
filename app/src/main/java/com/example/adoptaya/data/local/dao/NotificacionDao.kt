package com.example.adoptaya.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.adoptaya.data.model.Notificacion

@Dao
interface NotificacionDao {
    // Trae solo las notificaciones del usuario logueado, ordenadas de la mas nueva a la mas vieja
    @Query("SELECT * FROM notificaciones WHERE idUsuario = :usuarioId OR (tipo = 'NUEVA_MASCOTA' AND idUsuarioEmisor != :usuarioId) ORDER BY fechaHora DESC")
    suspend fun obtenerPorUsuario(usuarioId: String): List<Notificacion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNotificacion(notificacion: Notificacion)

    @Query("SELECT EXISTS(SELECT 1 FROM notificaciones WHERE id = :id)")
    suspend fun existeNotificacion(id: String): Boolean
}