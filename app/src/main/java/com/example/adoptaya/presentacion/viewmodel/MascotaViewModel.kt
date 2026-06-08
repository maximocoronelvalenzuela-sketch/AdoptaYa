package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotaPorIdUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotasUseCase
import kotlinx.coroutines.launch

class MascotaViewModel (
    private val obtenerMascotasUseCase: ObtenerMascotasUseCase,
    private val obtenerMascotaPorIdUseCase: ObtenerMascotaPorIdUseCase
) : ViewModel() {
    var mascotas by mutableStateOf<List<Mascota>>(emptyList())
        private set // Propiedad con getter público y setter privado para gestionar el estado de Compose.

    var mascotaSeleccionada by mutableStateOf<Mascota?>(null)
        private set

    fun cargarMascotas() {
        // Abrimos una corrutina para ejecutar la función suspendida de Room
        viewModelScope.launch {
            mascotas = obtenerMascotasUseCase()
        }
    }

    fun cargarMascotaPorId(id: String) {
        // Igual aqui, la busqueda por ID en base de datos requiere corrutina
        viewModelScope.launch {
            mascotaSeleccionada = obtenerMascotaPorIdUseCase(id)
        }
    }
}