package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.dominio.usecase.notificacion.ObtenerNotificacionesPorUsuario
import kotlinx.coroutines.launch

class NotificacionViewModel (
    private val obtenerNotificacionesPorUsuario: ObtenerNotificacionesPorUsuario
): ViewModel() {
    var notificacionesDeUsuario by mutableStateOf<List<Notificacion>>(emptyList())
        private set

    fun cargarNotificacionesDeUsuario(idUsuario: String) {
        viewModelScope.launch {
            notificacionesDeUsuario = obtenerNotificacionesPorUsuario(idUsuario)
        }
    }
}