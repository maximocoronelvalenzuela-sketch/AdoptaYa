package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.local.dao.FavoritoDao
import com.example.adoptaya.data.model.Favorito
import com.example.adoptaya.data.remoto.FavoritoServicio
import com.example.adoptaya.dominio.repositorio.FavoritoRepositorio

class FavoritoRepositorioImpl(
    private val favoritoDao: FavoritoDao
) : FavoritoRepositorio {

    override suspend fun obtenerFavoritosPorUsuario(idUsuario: String): List<String> {
        return favoritoDao.obtenerFavoritosPorUsuario(idUsuario)
    }

    override suspend fun agregar(idUsuario: String, idMascota: String) {
        favoritoDao.insertarFavorito(Favorito(idUsuario, idMascota))
    }

    override suspend fun eliminar(idUsuario: String, idMascota: String) {
        favoritoDao.eliminarFavorito(Favorito(idUsuario, idMascota))
    }
}