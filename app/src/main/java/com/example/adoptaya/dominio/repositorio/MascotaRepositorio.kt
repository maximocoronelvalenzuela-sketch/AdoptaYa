package com.example.adoptaya.dominio.repositorio

import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota

interface MascotaRepositorio {
    suspend fun obtenerMascotas(): List<Mascota>

    suspend fun obtenerMascotaPorId(id: String): Mascota?

    suspend fun filtrarMascotasPorTipo(tipo: String): List<Mascota>

    suspend fun filtrarMascotasPorSexo(sexo: String): List<Mascota>

    suspend fun guardarMascota(mascota: Mascota)

    suspend fun darDeBajaMascota(idMascota: String)

    suspend fun actualizarEstadoMascota(idMascota: String, nuevoEstado: Enums.EstadoMascota)
}