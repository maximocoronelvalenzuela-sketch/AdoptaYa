package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.usecase.favorito.ObtenerFavoritosPorUsuarioUseCase

class FavoritoViewModel (
    private val obtenerFavoritoPorUsuarioUseCase: ObtenerFavoritosPorUsuarioUseCase
) {
    var favoritosDeUsuario by mutableStateOf<List<Mascota>>(emptyList())
        private set

    fun cargarFavoritosDeUsuario(idUsuario: String) {
        favoritosDeUsuario = obtenerFavoritoPorUsuarioUseCase(idUsuario)
    }
}