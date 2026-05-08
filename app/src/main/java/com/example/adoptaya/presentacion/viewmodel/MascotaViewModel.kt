package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotaPorIdUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotasUseCase

class MascotaViewModel (
    private val obtenerMascotasUseCase: ObtenerMascotasUseCase,
    private val obtenerMascotaPorIdUseCase: ObtenerMascotaPorIdUseCase
) : ViewModel() {
    var mascotas by mutableStateOf<List<Mascota>>(emptyList())
        private set // Propiedad con getter público y setter privado para gestionar el estado de Compose.

    var mascotaSeleccionada by mutableStateOf<Mascota?>(null)
        private set

    fun cargarMascotas() {
        mascotas = obtenerMascotasUseCase()
    }

    fun cargarMascotaPorId(id: String) {
        mascotaSeleccionada = obtenerMascotaPorIdUseCase(id)
    }
}