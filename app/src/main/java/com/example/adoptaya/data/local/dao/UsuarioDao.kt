package com.example.adoptaya.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.adoptaya.data.model.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id = :usuarioId")
    suspend fun obtenerPorId(usuarioId: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)
}