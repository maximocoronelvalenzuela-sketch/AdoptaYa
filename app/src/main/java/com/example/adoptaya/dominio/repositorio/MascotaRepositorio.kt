package com.example.adoptaya.dominio.repositorio

import com.example.adoptaya.data.model.Mascota

interface MascotaRepositorio {
    fun obtenerMascotas(): List<Mascota>

    fun obtenerMascotaPorId(id: String): Mascota?
}