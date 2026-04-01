package com.example.adoptaya.dominio.usecase

import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class ObtenerMascotasUseCase (
    private val repository: MascotaRepositorio
) {

    operator fun invoke(): List<Mascota> {
        return repository.obtenerMascotas()
    }
}