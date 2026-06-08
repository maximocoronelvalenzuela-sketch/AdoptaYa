package com.example.adoptaya.dominio.usecase.mascota

import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class ObtenerMascotasUseCase (
    private val repository: MascotaRepositorio
) {

    operator suspend fun invoke(): List<Mascota> {
        return repository.obtenerMascotas()
    }
}