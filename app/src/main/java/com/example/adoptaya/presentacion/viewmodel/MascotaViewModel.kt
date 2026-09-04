package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.usecase.mascota.ActualizarEstadoPublicacionUseCase
import com.example.adoptaya.dominio.usecase.mascota.DarDeBajaMascotaUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotaPorIdUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotasUseCase
import kotlinx.coroutines.launch

class MascotaViewModel (
    private val obtenerMascotasUseCase: ObtenerMascotasUseCase,
    private val obtenerMascotaPorIdUseCase: ObtenerMascotaPorIdUseCase,
    private val darDeBajaMascotaUseCase: DarDeBajaMascotaUseCase,
    private val actualizarEstadoPublicacionUseCase: ActualizarEstadoPublicacionUseCase
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

    fun obtenerTiempoTranscurrido(fechaHoraString: String): String {
        return try {
            val fechaAlta = java.time.LocalDateTime.parse(fechaHoraString, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val ahora = java.time.LocalDateTime.now()
            val minutos = java.time.Duration.between(fechaAlta, ahora).toMinutes()

            when {
                minutos < 60 -> "hace $minutos min"
                minutos < 1440 -> "hace ${minutos / 60} h"
                else -> "hace ${minutos / 1440} d"
            }
        } catch (e: Exception) {
            "hace un momento"
        }
    }

    fun borrarPublicacion(idMascota: String) {
        viewModelScope.launch {
            darDeBajaMascotaUseCase(idMascota)
            cargarMascotas()
        }
    }

    fun cambiarEstadoPublicacion(idMascota: String, nuevoEstado: Enums.EstadoMascota) {
        viewModelScope.launch {
            actualizarEstadoPublicacionUseCase(idMascota, nuevoEstado)
            cargarMascotas()
        }
    }
}