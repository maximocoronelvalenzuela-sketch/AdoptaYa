package com.example.adoptaya.dominio.repositorio

import com.example.adoptaya.data.model.Favorito

interface FavoritoRepositorio {
    fun obtenerFavoritos(): List<Favorito>

    fun obtenerFavoritosPorUsuario(idUsuario: String): List<Favorito>

}