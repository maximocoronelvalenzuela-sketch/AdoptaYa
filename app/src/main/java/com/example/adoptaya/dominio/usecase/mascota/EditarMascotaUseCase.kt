package com.example.adoptaya.dominio.usecase.mascota

import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class EditarMascotaUseCase(
    private val repositorio: MascotaRepositorio
) {
    suspend operator fun invoke(mascota: Mascota) {
        repositorio.editarMascota(mascota)
    }
}