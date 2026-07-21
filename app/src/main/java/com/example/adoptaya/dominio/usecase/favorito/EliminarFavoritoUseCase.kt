package com.example.adoptaya.dominio.usecase.favorito

import com.example.adoptaya.dominio.repositorio.FavoritoRepositorio

class EliminarFavoritoUseCase(
    private val favoritoRepositorio: FavoritoRepositorio
) {
    suspend operator fun invoke(idUsuario: String, idMascota: String) {
        favoritoRepositorio.eliminar(idUsuario, idMascota)
    }
}