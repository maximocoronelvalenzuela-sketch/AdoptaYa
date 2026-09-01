package com.example.adoptaya.presentacion.viewmodel

import android.util.Log
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
import com.example.adoptaya.dominio.usecase.auth.RecuperarPasswordUseCase
import com.example.adoptaya.dominio.usecase.auth.RegistrarUseCase
import com.example.adoptaya.dominio.usecase.usuario.CrearUsuarioUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AuthViewModel(
    private val iniciarSesionUseCase: IniciarSesionUseCase,
    private val registrarUseCase: RegistrarUseCase,
    private val obtenerIdUsuarioActualUseCase: ObtenerIdUsuarioActualUseCase,
    private val crearUsuarioUseCase: CrearUsuarioUseCase,
    private val cerrarSesionUseCase: CerrarSesionUseCase,
    private val recuperarPasswordUseCase: RecuperarPasswordUseCase
) : ViewModel() {

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set
    var mensajeErrorNombre by mutableStateOf<String?>(null)
        private set
    var mensajeErrorEmail by mutableStateOf<String?>(null)
        private set
    var mensajeErrorPassword by mutableStateOf<String?>(null)
        private set
    var mensajeErrorTelefono by mutableStateOf<String?>(null)
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

    fun iniciarSesion(email: String, password: String, onExito: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = null

            val resultado = iniciarSesionUseCase(email, password)
            resultado.onSuccess { uid ->
                idUsuarioActual = uid
                onExito()
            }.onFailure {
                mensajeError = "El correo o la contraseña son incorrectos."
            }

            estaCargando = false
        }
    }

    fun registrar(nombre: String, email: String, password: String, telefono: String, onExito: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeErrorNombre = null
            mensajeErrorEmail = null
            mensajeErrorPassword = null
            mensajeErrorTelefono = null

            var todoValido = true
            if (nombre.isBlank()) {
                mensajeErrorNombre = "El nombre no puede estar vacío"
                todoValido = false
            }
            if (!email.lowercase().endsWith("@gmail.com")) {
                mensajeErrorEmail = "El eMail debe ser de Google (@gmail.com)"
                todoValido = false
            }
            if (password.length < 6) {
                mensajeErrorPassword = "La contraseña debe tener al menos 6 caracteres"
                todoValido = false
            }
            if (telefono.isBlank()) {
                mensajeErrorTelefono = "El teléfono no puede estar vacío"
                todoValido = false
            } else if (!esTelefonoValido(telefono)) {
                mensajeErrorTelefono = "Número inválido. Verificá tu código de área."
                todoValido = false
            }

            if (todoValido) {
                val telefonoCompleto = "549$telefono"

                estaCargando = true
                val resultado = registrarUseCase(nombre, email, password, telefonoCompleto)

                resultado.onSuccess { uid ->
                    idUsuarioActual = uid
                    val nuevoUsuario = Usuario(
                        id = uid,
                        nombre = nombre,
                        email = email,
                        telefono = telefonoCompleto,
                        latitud = 0.0,  // Provisorio hasta que edite su perfil
                        longitud = 0.0, // Provisorio
                        provincia = "", // Provisorio
                        ciudad = "",    // Provisorio
                        imagen = null,  // Provisorio
                        fechaHoraAlta = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    )
                    crearUsuarioUseCase(nuevoUsuario)
                    onExito()
                }.onFailure { excepcion ->
                    if (excepcion is com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                        mensajeError = "Este correo electrónico ya está registrado."
                    } else {
                        mensajeError = "Ocurrió un error al registrar la cuenta. Intentá de nuevo."
                    }
                }
                estaCargando = false
            }
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

    private fun esTelefonoValido(numero: String): Boolean {
        // Herramienta principal para validar numeros de telefono
        val phoneUtil = PhoneNumberUtil.getInstance()
        try {
            val numeroInternacional = "+549$numero"
            // Parsea el String a un PhoneNumber que ya entiende de los codigos de area y el prefijo de Argentina
            val numeroProto = phoneUtil.parse(numeroInternacional, "AR")

            // Valida que el numero sea valido y que sea de tipo celular (y no un telefono fijo, por ejemplo)
            return phoneUtil.isValidNumber(numeroProto) && phoneUtil.getNumberType(numeroProto) == PhoneNumberUtil.PhoneNumberType.MOBILE
        } catch (e: Exception) {
            return false
        }
    }

    fun limpiarError(campo: String) {
        when(campo) {
            "nombre" -> mensajeErrorNombre = null
            "email" -> mensajeErrorEmail = null
            "password" -> mensajeErrorPassword = null
            "telefono" -> mensajeErrorTelefono = null
        }
    }

    fun enviarCorreoRecuperacion(
        email: String,
        onExito: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            estaCargando = true

            val resultado = recuperarPasswordUseCase(email)

            resultado.onSuccess {
                estaCargando = false
                onExito()
            }.onFailure { excepcion ->
                estaCargando = false
                // Si Firebase devuelve un error (ej: el usuario no existe)
                onError(excepcion.message ?: "Error al enviar el correo")
            }
        }
    }
}