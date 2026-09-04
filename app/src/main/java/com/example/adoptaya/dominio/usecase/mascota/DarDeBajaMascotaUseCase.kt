package com.example.adoptaya.dominio.usecase.mascota

import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class DarDeBajaMascotaUseCase (
    private val repositorio: MascotaRepositorio
) {
    suspend operator fun invoke(idMascota: String) {
        repositorio.darDeBajaMascota(idMascota)
    }
}