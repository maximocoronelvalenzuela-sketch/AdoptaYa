package com.example.adoptaya.dominio.repositorio

import com.example.adoptaya.data.model.Favorito

interface FavoritoRepositorio {
    suspend fun obtenerFavoritosPorUsuario(idUsuario: String): List<String>

    suspend fun agregar(idUsuario: String, idMascota: String)

    suspend fun eliminar(idUsuario: String, idMascota: String)
}