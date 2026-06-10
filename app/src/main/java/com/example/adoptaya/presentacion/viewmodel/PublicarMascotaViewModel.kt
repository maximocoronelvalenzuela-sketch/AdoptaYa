package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.EstadoMascota
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
    var tipo by mutableStateOf("Perro") // Lista
    var raza by mutableStateOf("")
    var sexo by mutableStateOf("Macho")
    var edadNumero by mutableStateOf("")
    var edadTiempo by mutableStateOf("Años") // Lista (Meses/Años)
    var tamano by mutableStateOf("Chico") // Dropdown

    // Salud
    var vacunado by mutableStateOf(false)
    var esterilizado by mutableStateOf(false)
    var desparasitado by mutableStateOf(false)

    // Rasgos
    var rasgosSeleccionados by mutableStateOf(emptyList<String>())
    val rasgosDisponibles = listOf("Amigable", "Activo", "Sociable", "Cariñoso", "Tranquilo", "Entrenado")

    // Ubicacion
    var barrio by mutableStateOf("")
    var provincia by mutableStateOf("")
    var ciudad by mutableStateOf("")

    // Descripcion
    var descripcionAdicional by mutableStateOf("")

    fun toggleRasgo(rasgo: String) {
        rasgosSeleccionados = if (rasgosSeleccionados.contains(rasgo)) {
            rasgosSeleccionados - rasgo
        } else {
            rasgosSeleccionados + rasgo
        }
    }

    fun guardarMascota(onSuccess: () -> Unit) {
        val nuevaMascota = Mascota(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            tipo = tipo,
            edad = "$edadNumero $edadTiempo", // Une "3" + "Años"
            sexo = sexo,
            raza = raza.ifEmpty { null },
            tamano = tamano,
            esterilizado = esterilizado,
            vacunado = vacunado,
            desparasitado = desparasitado,
            descripcionPersonalidad = rasgosSeleccionados,
            descripcionAdicional = descripcionAdicional,
            estado = EstadoMascota.DISPONIBLE,
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
            onSuccess()
        }
    }
}