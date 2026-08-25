package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.dominio.usecase.auth.CerrarSesionUseCase
import com.example.adoptaya.dominio.usecase.auth.IngresarComoInvitadoUseCase
import com.example.adoptaya.dominio.usecase.auth.IniciarSesionUseCase
import com.example.adoptaya.dominio.usecase.auth.ObtenerIdUsuarioActualUseCase
import com.example.adoptaya.dominio.usecase.auth.RegistrarUseCase
import com.example.adoptaya.dominio.usecase.usuario.CrearUsuarioUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AuthViewModel(
    private val iniciarSesionUseCase: IniciarSesionUseCase,
    private val registrarUseCase: RegistrarUseCase,
    private val ingresarComoInvitadoUseCase: IngresarComoInvitadoUseCase,
    private val obtenerIdUsuarioActualUseCase: ObtenerIdUsuarioActualUseCase,
    private val crearUsuarioUseCase: CrearUsuarioUseCase,
    private val cerrarSesionUseCase: CerrarSesionUseCase
) : ViewModel() {

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    var idUsuarioActual by mutableStateOf<String?>(null)
        private set

    init {
        // Al arrancar, verifica si ya hay alguien logueado
        verificarSesion()
    }

    private fun verificarSesion() {
        idUsuarioActual = obtenerIdUsuarioActualUseCase()
    }

    fun limpiarError() {
        mensajeError = null
    }

    fun iniciarSesion(email: String, password: String, onExito: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = null

            val resultado = iniciarSesionUseCase(email, password)
            resultado.onSuccess { uid ->
                idUsuarioActual = uid
                onExito() // Navega a la pantalla principal
            }.onFailure { excepcion ->
                mensajeError = "El correo o la contraseña son incorrectos."
            }

            estaCargando = false
        }
    }

    fun registrar(nombre: String, email: String, password: String, onExito: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = null

            val resultado = registrarUseCase(nombre, email, password)
            resultado.onSuccess { uid ->
                idUsuarioActual = uid

                val nuevoUsuario = Usuario(
                    id = uid,
                    nombre = nombre,
                    email = email,
                    password = password,
                    telefono = "",  // Provisorio hasta que edite su perfil
                    latitud = 0.0,  // Provisorio
                    longitud = 0.0, // Provisorio
                    provincia = "", // Provisorio
                    ciudad = "",    // Provisorio
                    barrio = "",    // Provisorio
                    imagen = null,  // Provisorio
                    fechaHoraAlta = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )

                crearUsuarioUseCase(nuevoUsuario)
                onExito()
            }.onFailure { excepcion ->
                mensajeError = excepcion.message ?: "Error al registrarse"
            }

            estaCargando = false
        }
    }

    fun ingresarComoInvitado(onExito: () -> Unit) {
        cerrarSesionUseCase()
        idUsuarioActual = null
        onExito()
    }

    fun cerrarSesion(onExito: () -> Unit) {
        cerrarSesionUseCase()
        idUsuarioActual = null
        onExito()
    }
}