package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.data.remoto.MascotaServicio
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class MascotaRepositorioImpl (
    private val servicio: MascotaServicio
) : MascotaRepositorio {

    override fun obtenerMascotas(): List<Mascota> {
        return servicio.obtenerMascotas()
    }

    override fun obtenerMascotaPorId(id: String): Mascota? {
        return servicio.obtenerMascotaPorId(id)
    }

}