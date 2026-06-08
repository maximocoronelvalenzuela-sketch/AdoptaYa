package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.usecase.favorito.ObtenerFavoritosPorUsuarioUseCase
import kotlinx.coroutines.launch

class FavoritoViewModel (
    private val obtenerFavoritoPorUsuarioUseCase: ObtenerFavoritosPorUsuarioUseCase
) : ViewModel() {
    var favoritosDeUsuario by mutableStateOf<List<Mascota>>(emptyList())
        private set

    fun cargarFavoritosDeUsuario(idUsuario: String) {
        viewModelScope.launch {
            favoritosDeUsuario = obtenerFavoritoPorUsuarioUseCase(idUsuario)
        }
    }
}