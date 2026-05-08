package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.dominio.usecase.usuario.ObtenerUsuarioPorIdUseCase

class UsuarioViewModel (
    private val obtenerUsuarioPorIdUseCase: ObtenerUsuarioPorIdUseCase
) : ViewModel() {
    var usuarioSeleccionado by mutableStateOf<Usuario?>(null)
        private set

    fun cargarUsuarioPorId(id: String) {
        usuarioSeleccionado = obtenerUsuarioPorIdUseCase(id)
    }
}