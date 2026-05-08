package com.example.adoptaya.dominio.usecase.favorito

import com.example.adoptaya.data.model.Favorito
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.repositorio.FavoritoRepositorio
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio

class ObtenerFavoritosPorUsuarioUseCase (
    private val favoritoRepositorio: FavoritoRepositorio,
    private val mascotaRepositorio: MascotaRepositorio
) {

    operator fun invoke(idUsuario: String): List<Mascota> {
        val favoritos = favoritoRepositorio.obtenerFavoritosPorUsuario(idUsuario)

        val mascotas = favoritos.mapNotNull { favorito ->
            mascotaRepositorio.obtenerMascotaPorId(favorito.idMascota)
        }

        return mascotas
    }
}