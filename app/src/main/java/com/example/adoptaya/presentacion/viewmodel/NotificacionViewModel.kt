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
    var filtroActual by mutableStateOf("Todas")
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
        imagenMascota: String?,
        hayInternet: Boolean,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hayInternet) {
            onError("No hay conexión a internet para enviar la solicitud.")
            return
        }

        val idNoti = UUID.randomUUID().toString()
        val nuevaNotificacion = Notificacion(
            id = idNoti,
            titulo = "Nueva solicitud de contacto",
            descripcion = "Alguien está interesado en adoptar a "+nombreMascota+". ¡Contactalo por WhatsApp!",
            imagen = imagenMascota,
            tipo = Enums.TipoNotificacion.CONTACTO,
            leida = false,
            fechaHora = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            idUsuario = idReceptor,       // El dueño de la mascota (U2)
            idMascota = idMascota,
            idUsuarioEmisor = idEmisor    // El que desea contactarse con el dueño (U1)
        )

        viewModelScope.launch {
            guardarNotificacion(nuevaNotificacion)
            onSuccess(idNoti) // Se manda un mensaje en la pantalla
        }
    }

    fun enviarNotificacionFavorito(idEmisor: String, idReceptor: String, idMascota: String, nombreMascota: String, imagenMascota: String?, onSuccess: (String) -> Unit) {
        val idNoti = UUID.randomUUID().toString()
        val noti = Notificacion(
            id = idNoti,
            titulo = "¡A alguien le gusta tu mascota!",
            descripcion = "$nombreMascota fue agregado a favoritos.",
            imagen = imagenMascota,
            tipo = Enums.TipoNotificacion.FAVORITO,
            leida = false,
            fechaHora = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            idUsuario = idReceptor,
            idMascota = idMascota,
            idUsuarioEmisor = idEmisor
        )
        viewModelScope.launch {
            guardarNotificacion(noti)
            onSuccess(idNoti)
        }
    }

    // Mandamos la notificación al mismo creador para la demostracion
    fun enviarNotificacionNuevaMascota(idDueño: String, idMascota: String, nombreMascota: String, imagenMascota: String?, onSuccess: (String) -> Unit) {
        val idNoti = UUID.randomUUID().toString()
        val noti = Notificacion(
            id = idNoti,
            titulo = "¡Nueva mascota publicada!",
            descripcion = "Se acaba de sumar "+nombreMascota+" y buscando un hogar.",
            imagen = imagenMascota,
            tipo = Enums.TipoNotificacion.NUEVA_MASCOTA,
            leida = false,
            fechaHora = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            idUsuario = idDueño,
            idMascota = idMascota,
            idUsuarioEmisor = idDueño
        )
        viewModelScope.launch {
            guardarNotificacion(noti)
            onSuccess(idNoti)
        }
    }

    fun cambiarFiltro(nuevoFiltro: String) {
        filtroActual = nuevoFiltro
    }

    val notificacionesFiltradas: List<Notificacion>
        get() = when (filtroActual) {
            "Solicitudes" -> notificacionesDeUsuario.filter { it.tipo == Enums.TipoNotificacion.CONTACTO }
            "Favoritos" -> notificacionesDeUsuario.filter { it.tipo == Enums.TipoNotificacion.FAVORITO }
            "Nuevas mascotas" -> notificacionesDeUsuario.filter { it.tipo == Enums.TipoNotificacion.NUEVA_MASCOTA }
            else -> notificacionesDeUsuario // "Todas"
        }

    fun marcarComoLeida(notificacion: Notificacion) {
        if (!notificacion.leida) {
            val notiLeida = notificacion.copy(leida = true)
            viewModelScope.launch {
                guardarNotificacion(notiLeida) // Se actualiza la notificacion existente
                notificacionesDeUsuario = notificacionesDeUsuario.map { if (it.id == notiLeida.id) notiLeida else it }
            }
        }
    }

    fun obtenerTiempoTranscurrido(fechaHoraString: String): String {
        return try {
            val fechaNoti = LocalDateTime.parse(fechaHoraString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val ahora = LocalDateTime.now()
            val minutos = java.time.Duration.between(fechaNoti, ahora).toMinutes()

            when {
                minutos < 60 -> "Hace $minutos min"
                minutos < 1440 -> "Hace ${minutos / 60} h" // 1440 mins = 24 horas
                else -> "Hace ${minutos / 1440} d" // Solo llega a contar por dias
            }
        } catch (e: Exception) {
            "Hace un momento"
        }
    }
}