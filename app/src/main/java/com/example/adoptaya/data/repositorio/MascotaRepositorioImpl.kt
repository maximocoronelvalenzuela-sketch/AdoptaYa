package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.local.dao.MascotaDao
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class MascotaRepositorioImpl(
    private val mascotaDao: MascotaDao
) : MascotaRepositorio {

    override suspend fun obtenerMascotas(): List<Mascota> {
        return mascotaDao.obtenerTodas()
    }

    override suspend fun obtenerMascotaPorId(id: String): Mascota? {
        return mascotaDao.obtenerPorId(id)
    }

    override suspend fun filtrarMascotasPorTipo(tipo: String): List<Mascota> {
        return mascotaDao.filtrarPorTipo(tipo)
    }

    override suspend fun filtrarMascotasPorSexo(sexo: String): List<Mascota> {
        return mascotaDao.filtrarPorSexo(sexo)
    }

    override suspend fun guardarMascota(mascota: Mascota) {
        mascotaDao.insertarMascota(mascota)
    }

    override suspend fun darDeBajaMascota(idMascota: String) {
        mascotaDao.darDeBajaMascota(idMascota)
    }

    override suspend fun actualizarEstadoMascota(idMascota: String, nuevoEstado: Enums.EstadoMascota) {
        mascotaDao.actualizarEstadoMascota(idMascota, nuevoEstado)
    }
}