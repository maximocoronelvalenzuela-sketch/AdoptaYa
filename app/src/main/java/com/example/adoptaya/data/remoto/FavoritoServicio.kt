package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Favorito

class FavoritoServicio {
    fun obtenerFavoritos(): List<Favorito> {
        return listOf(
            Favorito(
                idUsuario = "1",
                idMascota = "1"
            ),
            Favorito(
                idUsuario = "1",
                idMascota = "2"
            ),
            Favorito(
                idUsuario = "2",
                idMascota = "1"
            )
        )
    }

    fun obtenerFavoritosPorUsuario(idUsuario: String): List<Favorito> {
        return obtenerFavoritos().filter { it.idUsuario == idUsuario }
    }
}