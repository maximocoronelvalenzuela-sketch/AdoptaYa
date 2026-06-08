package com.example.adoptaya.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.adoptaya.data.model.Mascota

@Dao
interface MascotaDao {
    @Query("SELECT * FROM mascotas")
    suspend fun obtenerTodas(): List<Mascota>

    @Query("SELECT * FROM mascotas WHERE id = :mascotaId")
    suspend fun obtenerPorId(mascotaId: String): Mascota?

    @Query("SELECT * FROM mascotas WHERE tipo = :tipo")
    suspend fun filtrarPorTipo(tipo: String): List<Mascota>

    @Query("SELECT * FROM mascotas WHERE sexo = :sexo")
    suspend fun filtrarPorSexo(sexo: String): List<Mascota>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMascota(mascota: Mascota)

    @Delete
    suspend fun eliminarMascota(mascota: Mascota)
}