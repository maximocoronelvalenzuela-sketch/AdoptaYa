package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.local.dao.MascotaDao
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.data.remoto.RetrofitClient
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class MascotaRepositorioImpl(
    private val mascotaDao: MascotaDao
) : MascotaRepositorio {

    private val api = RetrofitClient.apiService

    override suspend fun obtenerMascotas(): List<Mascota> {
        try {
            val response = api.obtenerMascotas()
            if (response.isSuccessful) {
                response.body()?.let { mascotasRemotas ->
                    // Guarda en Room para mantener la caché local actualizada
                    for (mascota in mascotasRemotas) {
                        mascotaDao.insertarMascota(mascota)
                    }
                }
            }
        } catch (e: Exception) {
            // Si no hay internet, la app no se rompe y usa la caché local de Room
        }
        return mascotaDao.obtenerTodas()
    }

    override suspend fun obtenerMascotaPorId(id: String): Mascota? {
        try {
            val response = api.obtenerMascotaPorId(id)
            if (response.isSuccessful && response.body() != null) {
                val mascota = response.body()!!
                mascotaDao.insertarMascota(mascota)
                return mascota
            }
        } catch (e: Exception) {
            // Fallback a local si falla la red
        }
        return mascotaDao.obtenerPorId(id)
    }

    override suspend fun filtrarMascotasPorTipo(tipo: String): List<Mascota> {
        return mascotaDao.filtrarPorTipo(tipo)
    }

    override suspend fun filtrarMascotasPorSexo(sexo: String): List<Mascota> {
        return mascotaDao.filtrarPorSexo(sexo)
    }

    override suspend fun guardarMascota(mascota: Mascota) {
        // Guarda localmente primero para fluidez visual
        mascotaDao.insertarMascota(mascota)
        try {
            // Sincroniza con el backend en la nube para que otros celulares la vean
            api.publicarMascota(mascota)
        } catch (e: Exception) {
            // Manejo silencioso o log si falla la red en el momento
        }
    }

    override suspend fun darDeBajaMascota(idMascota: String) {
        mascotaDao.darDeBajaMascota(idMascota)
        try {
            val mascotaLocal = mascotaDao.obtenerPorId(idMascota)
            if (mascotaLocal != null) {
                api.actualizarMascota(idMascota, mascotaLocal)
            }
        } catch (e: Exception) {}
    }

    override suspend fun actualizarEstadoMascota(idMascota: String, nuevoEstado: Enums.EstadoMascota) {
        mascotaDao.actualizarEstadoMascota(idMascota, nuevoEstado)
        try {
            val mascotaLocal = mascotaDao.obtenerPorId(idMascota)
            if (mascotaLocal != null) {
                api.actualizarMascota(idMascota, mascotaLocal)
            }
        } catch (e: Exception) {}
    }
}