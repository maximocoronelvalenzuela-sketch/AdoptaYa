package com.example.adoptaya.data.repositorio

import android.util.Log
import com.example.adoptaya.data.local.dao.MascotaDao
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.data.remoto.RetrofitClient
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class MascotaRepositorioImpl(
    private val mascotaDao: MascotaDao
) : MascotaRepositorio {

    private val api = RetrofitClient.apiService
    private val TAG = "API_ADOPTAYA_MASCOTAS"

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
            Log.e(TAG, "Falla de red en obtenerMascotas, usando caché local de Room: ${e.message}", e)
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
            Log.e(TAG, "Falla de red en obtenerMascotaPorId: ${e.message}", e)
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
            Log.e(TAG, "No se pudo sincronizar la mascota con el backend: ${e.message}", e)
        }
    }

    override suspend fun darDeBajaMascota(idMascota: String) {
        mascotaDao.darDeBajaMascota(idMascota)
        try {
            val mascotaLocal = mascotaDao.obtenerPorId(idMascota)
            if (mascotaLocal != null) {
                api.actualizarMascota(idMascota, mascotaLocal)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al dar de baja en backend: ${e.message}", e)
        }
    }

    override suspend fun actualizarEstadoMascota(idMascota: String, nuevoEstado: Enums.EstadoMascota) {
        mascotaDao.actualizarEstadoMascota(idMascota, nuevoEstado)
        try {
            val mascotaLocal = mascotaDao.obtenerPorId(idMascota)
            if (mascotaLocal != null) {
                api.actualizarMascota(idMascota, mascotaLocal)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar estado en backend: ${e.message}", e)
        }
    }
}