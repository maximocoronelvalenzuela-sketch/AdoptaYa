package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.model.Favorito
import com.example.adoptaya.data.remoto.FavoritoServicio
import com.example.adoptaya.dominio.repositorio.FavoritoRepositorio

class FavoritoRepositorioImpl (
    private val servicio: FavoritoServicio
) : FavoritoRepositorio {

    override fun obtenerFavoritos(): List<Favorito> {
        return servicio.obtenerFavoritos()
    }

    override fun obtenerFavoritosPorUsuario(idUsuario: String): List<Favorito> {
        return servicio.obtenerFavoritosPorUsuario(idUsuario)
    }

}