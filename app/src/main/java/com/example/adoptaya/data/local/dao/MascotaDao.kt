package com.example.adoptaya.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.adoptaya.data.model.Mascota

@Dao
interface MascotaDao {
    @Query("SELECT * FROM mascotas WHERE activa = 1")
    suspend fun obtenerTodas(): List<Mascota>

    @Query("SELECT * FROM mascotas WHERE id = :mascotaId AND activa = 1")
    suspend fun obtenerPorId(mascotaId: String): Mascota?

    @Query("SELECT * FROM mascotas WHERE tipo = :tipo AND activa = 1")
    suspend fun filtrarPorTipo(tipo: String): List<Mascota>

    @Query("SELECT * FROM mascotas WHERE sexo = :sexo AND activa = 1")
    suspend fun filtrarPorSexo(sexo: String): List<Mascota>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMascota(mascota: Mascota)

    @Query("UPDATE mascotas SET activa = 0 WHERE id = :mascotaId")
    suspend fun darDeBajaMascota(mascotaId: String)

    @Delete
    suspend fun eliminarMascota(mascota: Mascota)
}