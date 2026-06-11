package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.usecase.mascota.GuardarMascotaUseCase
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PublicarMascotaViewModel(
    private val guardarMascotaUseCase: GuardarMascotaUseCase
) : ViewModel() {

    // Informacion Básica
    var nombre by mutableStateOf("")
    var tipo by mutableStateOf(Enums.TipoMascota.PERRO)
    var raza by mutableStateOf("")
    var sexo by mutableStateOf(Enums.SexoMascota.MACHO)
    var edad by mutableStateOf("")
    var tiempo by mutableStateOf(Enums.TiempoEdad.AÑOS)
    var tamaño by mutableStateOf(Enums.TamañoMascota.CHICO)

    // Salud
    var vacunado by mutableStateOf(false)
    var esterilizado by mutableStateOf(false)
    var desparasitado by mutableStateOf(false)

    // Rasgos
    var rasgoInput by mutableStateOf("")
    var rasgosSeleccionados by mutableStateOf(emptyList<String>())

    // Ubicacion
    var barrio by mutableStateOf("")
    var provincia by mutableStateOf("")
    var ciudad by mutableStateOf("")

    // Descripcion
    var descripcionAdicional by mutableStateOf("")

    // Mensajes de error
    var nombreError by mutableStateOf<String?>(null)
    var edadError by mutableStateOf<String?>(null)


    fun agregarRasgo() {
        val rasgoLimpio = rasgoInput.trim().replaceFirstChar { it.uppercase() }
        if (rasgoLimpio.isNotEmpty() && !rasgosSeleccionados.contains(rasgoLimpio)) {
            rasgosSeleccionados = rasgosSeleccionados + rasgoLimpio
        }
        rasgoInput = ""
    }

    fun eliminarRasgo(rasgo: String) {
        rasgosSeleccionados = rasgosSeleccionados - rasgo
    }

    // Valida y devuelve false si hay errores
    private fun validarDatos(): Boolean {
        var esValido = true

        // Validacion de Nombre
        if (nombre.trim().isBlank()) {
            nombreError = "El nombre no puede estar vacío"
            esValido = false
        } else {
            nombreError = null
        }

        // Validacion de Edad
        val edadInt = edad.trim().toIntOrNull()
        if (edad.trim().isBlank()) {
            edadError = "Ingrese la edad"
            esValido = false
        } else if (edadInt == null || edadInt <= 0) {
            edadError = "Debe ser un número mayor a 0"
            esValido = false
        } else {
            edadError = null
        }

        return esValido
    }

    fun guardarMascota(onSuccess: () -> Unit) {
        if (!validarDatos()) return

        val nuevaMascota = Mascota(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            tipo = tipo,
            edad = edad,
            tiempo = tiempo,
            sexo = sexo,
            raza = raza,
            tamaño = tamaño,
            esterilizado = esterilizado,
            vacunado = vacunado,
            desparasitado = desparasitado,
            descripcionPersonalidad = rasgosSeleccionados,
            descripcionAdicional = descripcionAdicional,
            estado = Enums.EstadoMascota.DISPONIBLE,
            latitud = -27.7833, // Hardcodeado temporalmente
            longitud = -64.2667, // Hardcodeado temporalmente
            provincia = provincia,
            ciudad = ciudad,
            barrio = barrio,
            imagenes = emptyList(), // Hardcodeado
            fechaHoraAlta = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            idUsuario = "1" // Hardcodeado
        )

        viewModelScope.launch {
            guardarMascotaUseCase(nuevaMascota)
            limpiarFormulario()
            onSuccess()
        }
    }

    fun limpiarFormulario() {
        nombre = ""
        tipo = Enums.TipoMascota.PERRO
        raza = ""
        sexo = Enums.SexoMascota.MACHO
        edad = ""
        tiempo = Enums.TiempoEdad.AÑOS
        tamaño = Enums.TamañoMascota.CHICO
        vacunado = false
        esterilizado = false
        desparasitado = false
        rasgosSeleccionados = emptyList()
        rasgoInput = ""
        barrio = ""
        provincia = ""
        ciudad = ""
        descripcionAdicional = ""
        nombreError = null
        edadError = null
    }
}