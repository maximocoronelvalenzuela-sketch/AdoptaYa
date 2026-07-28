package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.dominio.usecase.notificacion.GuardarNotificacionUseCase
import com.example.adoptaya.dominio.usecase.notificacion.ObtenerNotificacionesPorUsuario
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class NotificacionViewModel (
    private val obtenerNotificacionesPorUsuario: ObtenerNotificacionesPorUsuario,
    private val guardarNotificacion: GuardarNotificacionUseCase
) : ViewModel() {
    var notificacionesDeUsuario by mutableStateOf<List<Notificacion>>(emptyList())
        private set

    fun cargarNotificacionesDeUsuario(idUsuario: String) {
        viewModelScope.launch {
            notificacionesDeUsuario = obtenerNotificacionesPorUsuario(idUsuario)
        }
    }

    fun enviarSolicitudDeContacto(
        idEmisor: String,
        idReceptor: String,
        idMascota: String,
        nombreMascota: String,
        onSuccess: () -> Unit
    ) {
        val nuevaNotificacion = Notificacion(
            id = UUID.randomUUID().toString(),
            titulo = "Nueva solicitud de contacto",
            descripcion = "Alguien está interesado en adoptar a "+nombreMascota+". ¡Contactalo por WhatsApp!",
            imagen = null,
            tipo = Enums.TipoNotificacion.CONTACTO,
            leida = false,
            fechaHora = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            idUsuario = idReceptor,       // El dueño de la mascota (U2)
            idMascota = idMascota,
            idUsuarioEmisor = idEmisor    // El que desea contactarse con el dueño (U1)
        )

        viewModelScope.launch {
            guardarNotificacion(nuevaNotificacion)
            onSuccess() // Se manda un mensaje en la pantalla
        }
    }
}