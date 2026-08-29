package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.dominio.usecase.usuario.ObtenerUsuarioPorIdUseCase
import com.example.adoptaya.dominio.usecase.usuario.VerificarPerfilIncompletoUseCase
import kotlinx.coroutines.launch

class UsuarioViewModel (
    private val obtenerUsuarioPorIdUseCase: ObtenerUsuarioPorIdUseCase,
    private val verificarPerfilIncompletoUseCase: VerificarPerfilIncompletoUseCase
) : ViewModel() {
    var usuarioSeleccionado by mutableStateOf<Usuario?>(null)
        private set
    var perfilEstaIncompleto by mutableStateOf(false)
        private set

    fun cargarUsuarioPorId(id: String) {
        viewModelScope.launch {
            usuarioSeleccionado = obtenerUsuarioPorIdUseCase(id)

            perfilEstaIncompleto = verificarPerfilIncompletoUseCase(usuarioSeleccionado)
        }
    }

    fun limpiarUsuario() {
        usuarioSeleccionado = null
        perfilEstaIncompleto = false
    }
}