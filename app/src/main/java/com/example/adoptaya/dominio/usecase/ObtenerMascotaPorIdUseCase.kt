package com.example.adoptaya.dominio.usecase

import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class ObtenerMascotaPorIdUseCase (
    private val repositorio: MascotaRepositorio
) {

    operator fun invoke(id: String): Mascota? {
        return repositorio.obtenerMascotaPorId(id)
    }
}