package com.example.adoptaya.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.adoptaya.data.model.Favorito

@Dao
interface FavoritoDao {
    @Query("SELECT idMascota FROM favoritos WHERE idUsuario = :idUsuario")
    suspend fun obtenerFavoritosPorUsuario(idUsuario: String): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarFavorito(favorito: Favorito)

    @Delete
    suspend fun eliminarFavorito(favorito: Favorito)
}