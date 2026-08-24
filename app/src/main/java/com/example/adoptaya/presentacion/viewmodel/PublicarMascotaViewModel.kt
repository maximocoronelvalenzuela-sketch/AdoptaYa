package com.example.adoptaya.presentacion.viewmodel

import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.data.remoto.GeorefApi
import com.example.adoptaya.data.remoto.model.LocalidadGeoref
import com.example.adoptaya.data.remoto.model.ProvinciaGeoref
import com.example.adoptaya.dominio.usecase.auth.ObtenerIdUsuarioActualUseCase
import com.example.adoptaya.dominio.usecase.mascota.GuardarMascotaUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class PublicarMascotaViewModel(
    private val guardarMascotaUseCase: GuardarMascotaUseCase,
    private val obtenerIdUsuarioActualUseCase: ObtenerIdUsuarioActualUseCase
) : ViewModel() {
    private val apiGeoref = GeorefApi.crear()

    // Imagenes
    var imagenesSeleccionadas by mutableStateOf<List<String>>(emptyList())

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
    var latitud by mutableStateOf<Double?>(null)
    var longitud by mutableStateOf<Double?>(null)
    var barrio by mutableStateOf("")
    var provincia by mutableStateOf("")
    var ciudad by mutableStateOf("")
    var calle by mutableStateOf("")
    var numero by mutableStateOf("")
    // Listas para los Dropdown
    var listaProvincias by mutableStateOf(emptyList<ProvinciaGeoref>())
    var listaLocalidades by mutableStateOf(emptyList<LocalidadGeoref>())
    // Objetos seleccionados
    var provinciaSeleccionada by mutableStateOf<ProvinciaGeoref?>(null)
    var localidadSeleccionada by mutableStateOf<LocalidadGeoref?>(null)

    // Descripcion
    var descripcionAdicional by mutableStateOf("")

    // Mensajes de error
    var nombreError by mutableStateOf<String?>(null)
    var edadError by mutableStateOf<String?>(null)
    var ubicacionError by mutableStateOf<String?>(null)
    var barrioError by mutableStateOf<String?>(null)
    var calleError by mutableStateOf<String?>(null)
    var numeroError by mutableStateOf<String?>(null)
    var descripcionError by mutableStateOf<String?>(null)


    init {
        cargarProvincias()
    }

    private fun cargarProvincias() {
        viewModelScope.launch {
            try {
                val respuesta = apiGeoref.obtenerProvincias()
                listaProvincias = respuesta.provincias
            } catch (e: Exception) {
                ubicacionError = "Error al cargar provincias. Revisá tu conexión."
            }
        }
    }

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

        // Validacion de Mapa generado
        if (latitud == null || longitud == null) {
            ubicacionError = "Por favor, genere la ubicación en el mapa antes de publicar."
            esValido = false
        }

        // Validacion de Ubicacion
        if (barrio.trim().isBlank()) {
            barrioError = "El barrio es obligatorio"
            esValido = false
        } else {
            barrioError = null
        }

        if (calle.trim().isBlank()) {
            calleError = "La calle es obligatoria"
            esValido = false
        } else {
            calleError = null
        }

        if (numero.trim().isBlank()) {
            numeroError = "El número es obligatorio"
            esValido = false
        } else {
            numeroError = null
        }

        // Validacion de Descripcion
        if (descripcionAdicional.trim().isBlank()) {
            descripcionError = "Agregue una pequeña descripción"
            esValido = false
        } else {
            descripcionError = null
        }

        return esValido
    }

    fun guardarImagenEnLocal(context: android.content.Context, uri: android.net.Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        // Se crea un nombre unico para el archivo
        val archivoLocal = java.io.File(context.filesDir, "mascota_${UUID.randomUUID()}.jpg")
        val outputStream = java.io.FileOutputStream(archivoLocal)

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        return archivoLocal.absolutePath
    }

    fun cargarLocalidadesPorProvincia(provincia: ProvinciaGeoref) {
        viewModelScope.launch {
            try {
                val respuesta = apiGeoref.obtenerLocalidades(provincia.id)
                listaLocalidades = respuesta.localidades
                localidadSeleccionada = null // Reseteamos la ciudad si cambió de provincia
            } catch (e: Exception) {
                ubicacionError = "Error al cargar las ciudades."
            }
        }
    }

    fun generarCoordenadasDesdeDireccion(context: android.content.Context) {
        val direccionCompleta = "$calle $numero, $barrio, ${localidadSeleccionada?.nombre}, ${provinciaSeleccionada?.nombre}, Argentina"

        // Dispatchers.IO para que la busqueda no congele la pantalla
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                // Pedimos máximo 1 resultado
                val direcciones = geocoder.getFromLocationName(direccionCompleta, 1)

                if (!direcciones.isNullOrEmpty()) {
                    val ubicacionReal = direcciones[0]
                    // Se vuelve al hilo principal para actualizar la UI
                    launch(Dispatchers.Main) {
                        latitud = ubicacionReal.latitude
                        longitud = ubicacionReal.longitude
                        ubicacionError = null
                    }
                } else {
                    launch(Dispatchers.Main) {
                        ubicacionError = "No pudimos encontrar la dirección exacta en el mapa."
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    ubicacionError = "Error al buscar en el mapa. Verifica tu internet."
                }
            }
        }
    }

    fun guardarMascota(onSuccess: () -> Unit) {
        if (!validarDatos()) return

        val idActual = obtenerIdUsuarioActualUseCase()

        if(idActual == null) return

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
            latitud = latitud ?: 0.0,
            longitud = longitud ?: 0.0,
            provincia = provinciaSeleccionada?.nombre ?: "",
            ciudad = localidadSeleccionada?.nombre ?: "",
            barrio = barrio,
            calle = calle,
            numero = numero,
            imagenes = imagenesSeleccionadas,
            fechaHoraAlta = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            idUsuario = idActual
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
        latitud = null
        longitud = null
        provincia = ""
        ciudad = ""
        barrio = ""
        calle = ""
        numero = ""
        provinciaSeleccionada = null
        localidadSeleccionada = null
        descripcionAdicional = ""
        imagenesSeleccionadas = emptyList()
        nombreError = null
        edadError = null
        ubicacionError = null
        barrioError = null
        calleError = null
        numeroError = null
        descripcionError = null
    }
}