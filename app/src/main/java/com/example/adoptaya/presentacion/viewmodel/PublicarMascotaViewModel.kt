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
import com.example.adoptaya.dominio.usecase.mascota.EditarMascotaUseCase
import com.example.adoptaya.dominio.usecase.mascota.GuardarMascotaUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotaPorIdUseCase
import com.example.adoptaya.util.subirFotoAImgBB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class PublicarMascotaViewModel(
    private val guardarMascotaUseCase: GuardarMascotaUseCase,
    private val obtenerIdUsuarioActualUseCase: ObtenerIdUsuarioActualUseCase,
    private val obtenerMascotaPorIdUseCase: ObtenerMascotaPorIdUseCase,
    private val editarMascotaUseCase: EditarMascotaUseCase
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
    var rasgoError by mutableStateOf<String?>(null)
    var ubicacionError by mutableStateOf<String?>(null)
    var barrioError by mutableStateOf<String?>(null)
    var calleError by mutableStateOf<String?>(null)
    var numeroError by mutableStateOf<String?>(null)
    var descripcionError by mutableStateOf<String?>(null)

    // Para mascota editada
    var idMascotaEditada by mutableStateOf<String?>(null)
        private set
    var fechaHoraOriginal by mutableStateOf("")
        private set

    var estaPublicando by mutableStateOf(false)
        private set
    var estaCargandoMapa by mutableStateOf(false)
        private set
    var estaCargandoDatos by mutableStateOf(false)
        private set

    fun cargarProvincias(hayInternet: Boolean) {
        if (!hayInternet) {
            ubicacionError = "No hay conexión a internet para cargar provincias."
            return
        }
        ubicacionError = null

        viewModelScope.launch {
            try {
                val respuesta = apiGeoref.obtenerProvincias()
                listaProvincias = respuesta.provincias
            } catch (e: Exception) {
                ubicacionError = "Error al cargar provincias. Revisa tu conexión."
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

        // Validacion de Rasgos
        if (rasgosSeleccionados.isEmpty()) {
            rasgoError = "Debe ingresar al menos un Rasgo."
            esValido = false
        } else {
            rasgoError = null
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
        val archivoLocal = java.io.File(context.filesDir, "img_${java.util.UUID.randomUUID()}.jpg")
        try {
            val bitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                val source = android.graphics.ImageDecoder.createSource(context.contentResolver, uri)
                android.graphics.ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            // Compresión nivel demostración: 200px de ancho máximo
            val maxWidth = 200
            val ancho = if (bitmap.width > maxWidth) maxWidth else bitmap.width
            val alto = if (bitmap.width > maxWidth) (bitmap.height * (maxWidth.toFloat() / bitmap.width)).toInt() else bitmap.height
            val bitmapRedimensionado = android.graphics.Bitmap.createScaledBitmap(bitmap, ancho, alto, true)

            val outputStream = java.io.FileOutputStream(archivoLocal)
            // Calidad al 10% (pesará apenas unos pocos kilobytes)
            bitmapRedimensionado.compress(android.graphics.Bitmap.CompressFormat.JPEG, 10, outputStream)
            outputStream.flush()
            outputStream.close()

            return archivoLocal.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun cargarLocalidadesPorProvincia(provincia: ProvinciaGeoref, hayInternet: Boolean) {
        if (!hayInternet) {
            ubicacionError = "No hay conexión a internet para cargar las ciudades."
            return
        }
        ubicacionError = null

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

    fun generarCoordenadasDesdeDireccion(context: android.content.Context, hayInternet: Boolean) {
        if (!hayInternet) {
            ubicacionError = "No hay conexión a internet para buscar en el mapa."
            return
        }
        ubicacionError = null
        estaCargandoMapa = true

        val direccionCompleta = "$calle $numero, $barrio, ${localidadSeleccionada?.nombre}, ${provinciaSeleccionada?.nombre}, Argentina"

        // Dispatchers.IO para que la busqueda no congele la pantalla
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                // Pedimos máximo 1 resultado
                val direcciones = geocoder.getFromLocationName(direccionCompleta, 1)

                // Vuelve al hilo principal para actualizar la UI y APAGAR LA CARGA
                launch(Dispatchers.Main) {
                    if (!direcciones.isNullOrEmpty()) {
                        val ubicacionReal = direcciones[0]
                        latitud = ubicacionReal.latitude
                        longitud = ubicacionReal.longitude
                        ubicacionError = null
                    } else {
                        ubicacionError = "No pudimos encontrar la dirección exacta en el mapa."
                    }
                    estaCargandoMapa = false
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    ubicacionError = "Error al buscar en el mapa. Verifica tu internet."
                }
            }
        }
    }

    fun guardarMascota(onSuccess: (String) -> Unit) {
        if (!validarDatos()) return
        val idActual = obtenerIdUsuarioActualUseCase()
        if(idActual == null) return

        val esEdicion = !idMascotaEditada.isNullOrBlank()
        val idFinal = if (esEdicion) idMascotaEditada!! else UUID.randomUUID().toString()
        val fechaAlta = if (esEdicion) fechaHoraOriginal else LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        viewModelScope.launch {
            estaPublicando = true

            // Sube las fotos locales a ImgBB de forma asíncrona y obtiene las URLs públicas
            val urlsPublicas = imagenesSeleccionadas.filter { it.isNotBlank() }.mapNotNull { rutaLocal ->
                // Si la URL ya empieza con http (es decir, ya estaba subida y no se modificó), la devuelve directo
                if (rutaLocal.startsWith("http")) rutaLocal else subirFotoAImgBB(rutaLocal)
            }

            val mascotaFinal = Mascota(
                id = idFinal,
                nombre = nombre.capitalizarPalabras(),
                tipo = tipo,
                edad = edad,
                tiempo = tiempo,
                sexo = sexo,
                raza = raza.capitalizarPalabras(),
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
                barrio = barrio.capitalizarPalabras(),
                calle = calle.capitalizarPalabras(),
                numero = numero,
                imagenes = urlsPublicas,
                fechaHoraAlta = fechaAlta,
                idUsuario = idActual
            )

            // Guarda en Room y en el backend
            if (esEdicion) {
                editarMascotaUseCase(mascotaFinal)
            } else {
                guardarMascotaUseCase(mascotaFinal)
            }
            limpiarFormulario()
            onSuccess(idFinal)
            estaPublicando = false
        }
    }

    fun limpiarFormulario() {
        idMascotaEditada = null
        fechaHoraOriginal = ""
        imagenesSeleccionadas = emptyList()
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
        rasgoError = null
        ubicacionError = null
        barrioError = null
        calleError = null
        numeroError = null
        descripcionError = null
        estaCargandoDatos = false
    }

    fun cargarMascotaParaEditar(idMascota: String) {
        viewModelScope.launch {
            estaCargandoDatos = true

            val mascota = obtenerMascotaPorIdUseCase(idMascota)
            mascota?.let { m ->
                idMascotaEditada = m.id
                fechaHoraOriginal = m.fechaHoraAlta
                imagenesSeleccionadas = m.imagenes
                nombre = m.nombre
                tipo = m.tipo
                raza = m.raza ?: ""
                sexo = m.sexo
                edad = m.edad
                tiempo = m.tiempo
                tamaño = m.tamaño

                vacunado = m.vacunado
                esterilizado = m.esterilizado
                desparasitado = m.desparasitado

                rasgosSeleccionados = m.descripcionPersonalidad
                descripcionAdicional = m.descripcionAdicional

                latitud = m.latitud
                longitud = m.longitud
                provincia = m.provincia
                ciudad = m.ciudad
                barrio = m.barrio
                calle = m.calle
                numero = m.numero

                if (listaProvincias.isEmpty()) {
                    try {
                        val respuesta = apiGeoref.obtenerProvincias()
                        listaProvincias = respuesta.provincias
                    } catch (e: Exception) {

                    }
                }

                // Busca el objeto Provincia que coincida con el texto guardado
                provinciaSeleccionada = listaProvincias.find { it.nombre == m.provincia }

                // Si encontra la provincia, obtiene sus ciudades y obtiene la seleccionada
                if (provinciaSeleccionada != null) {
                    try {
                        val respuestaLoc = apiGeoref.obtenerLocalidades(provinciaSeleccionada!!.id)
                        listaLocalidades = respuestaLoc.localidades
                        localidadSeleccionada = listaLocalidades.find { it.nombre == m.ciudad }
                    } catch (e: Exception) { }
                }
            }
            estaCargandoDatos = false
        }
    }

    private fun String.capitalizarPalabras(): String {
        return this.trim().lowercase().split(" ").joinToString(" ") {
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }

    fun actualizarImagenEnIndice(indice: Int, ruta: String) {
        val nuevaLista = imagenesSeleccionadas.toMutableList()
        // Rellena con vacíos si el usuario toca la foto 3 antes que la 2
        while (nuevaLista.size <= indice) {
            nuevaLista.add("")
        }
        nuevaLista[indice] = ruta
        imagenesSeleccionadas = nuevaLista.toList()
    }

    fun limpiarDetallesUbicacion() {
        barrio = ""
        calle = ""
        numero = ""
        barrioError = null
        calleError = null
        numeroError = null
        latitud = null
        longitud = null
    }
}