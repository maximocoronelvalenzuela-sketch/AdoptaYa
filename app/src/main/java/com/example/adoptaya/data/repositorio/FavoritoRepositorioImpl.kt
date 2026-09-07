package com.example.adoptaya.data.repositorio

import android.util.Log
import com.example.adoptaya.data.local.dao.FavoritoDao
import com.example.adoptaya.data.model.Favorito
import com.example.adoptaya.data.remoto.RetrofitClient
import com.example.adoptaya.dominio.repositorio.FavoritoRepositorio

class FavoritoRepositorioImpl(
    private val favoritoDao: FavoritoDao
) : FavoritoRepositorio {

    private val api = RetrofitClient.apiService
    private val TAG = "API_ADOPTAYA_FAVORITOS"

    override suspend fun obtenerFavoritosPorUsuario(idUsuario: String): List<String> {
        try {
            val response = api.obtenerFavoritosPorUsuario(idUsuario)
            if (response.isSuccessful) {
                response.body()?.let { mascotasIds ->
                    // Para evitar duplicados, limpiamos y recargamos los favoritos del usuario
                    favoritoDao.obtenerFavoritosPorUsuario(idUsuario).forEach { idMascota ->
                        favoritoDao.eliminarFavorito(Favorito(idUsuario, idMascota))
                    }
                    mascotasIds.forEach { idMascota ->
                        favoritoDao.insertarFavorito(Favorito(idUsuario, idMascota))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error de red en favoritos, usando caché: ${e.message}")
        }
        return favoritoDao.obtenerFavoritosPorUsuario(idUsuario)
    }

    override suspend fun agregar(idUsuario: String, idMascota: String) {
        favoritoDao.insertarFavorito(Favorito(idUsuario, idMascota))
        try {
            val body = mapOf("idUsuario" to idUsuario, "idMascota" to idMascota)
            api.agregarFavorito(body)
        } catch (e: Exception) {
            Log.e(TAG, "No se pudo agregar favorito: ${e.message}")
        }
    }

    override suspend fun eliminar(idUsuario: String, idMascota: String) {
        favoritoDao.eliminarFavorito(Favorito(idUsuario, idMascota))
        try {
            api.eliminarFavorito(idUsuario, idMascota)
        } catch (e: Exception) {
            Log.e(TAG, "No se pudo eliminar favorito en backend: ${e.message}")
        }
    }
}