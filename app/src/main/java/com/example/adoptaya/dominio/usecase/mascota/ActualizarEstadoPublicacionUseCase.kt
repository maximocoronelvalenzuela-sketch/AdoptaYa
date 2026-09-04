package com.example.adoptaya.dominio.usecase.mascota

import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class ActualizarEstadoPublicacionUseCase (
    private val repositorio: MascotaRepositorio
) {
    suspend operator fun invoke(idMascota: String, nuevoEstado: Enums.EstadoMascota) {
        repositorio.actualizarEstadoMascota(idMascota, nuevoEstado)
    }
}