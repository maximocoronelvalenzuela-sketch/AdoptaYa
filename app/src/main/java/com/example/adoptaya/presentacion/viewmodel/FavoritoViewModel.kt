package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.usecase.favorito.AgregarFavoritoUseCase
import com.example.adoptaya.dominio.usecase.favorito.EliminarFavoritoUseCase
import com.example.adoptaya.dominio.usecase.favorito.ObtenerFavoritosPorUsuarioUseCase
import kotlinx.coroutines.launch

class FavoritoViewModel (
    private val obtenerFavoritoPorUsuarioUseCase: ObtenerFavoritosPorUsuarioUseCase,
    private val agregarFavoritoUseCase: AgregarFavoritoUseCase,
    private val eliminarFavoritoUseCase: EliminarFavoritoUseCase
) : ViewModel() {
    var favoritosDeUsuario by mutableStateOf<List<Mascota>>(emptyList())
        private set

    fun cargarFavoritosDeUsuario(idUsuario: String) {
        viewModelScope.launch {
            favoritosDeUsuario = obtenerFavoritoPorUsuarioUseCase(idUsuario)
        }
    }

    // Comprueba si una mascota está en la lista de favoritos actual
    fun esFavorito(idMascota: String): Boolean {
        return favoritosDeUsuario.any { it.id == idMascota }
    }

    // Agrega o quita de la base de datos y de favoritosDeUsuario
        fun toggleFavorito(idUsuario: String, mascota: Mascota) {
        viewModelScope.launch {
            val yaEsFavorito = esFavorito(mascota.id)

            if (yaEsFavorito) {
                eliminarFavoritoUseCase(idUsuario, mascota.id)
                // Quitamos la mascota de la lista actual en la UI
                favoritosDeUsuario = favoritosDeUsuario.filter { it.id != mascota.id }
            } else {
                agregarFavoritoUseCase(idUsuario, mascota.id)
                // Sumamos la mascota a la lista actual en la UI
                favoritosDeUsuario = favoritosDeUsuario + mascota
            }
        }
    }
}