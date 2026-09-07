package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.data.remoto.GeorefApi
import com.example.adoptaya.data.remoto.model.LocalidadGeoref
import com.example.adoptaya.data.remoto.model.ProvinciaGeoref
import com.example.adoptaya.dominio.usecase.usuario.ActualizarUsuarioUseCase
import com.example.adoptaya.dominio.usecase.usuario.ObtenerUsuarioPorIdUseCase
import com.example.adoptaya.dominio.usecase.usuario.VerificarPerfilIncompletoUseCase
import com.example.adoptaya.util.subirFotoAImgBB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuarioViewModel (
    private val obtenerUsuarioPorIdUseCase: ObtenerUsuarioPorIdUseCase,
    private val verificarPerfilIncompletoUseCase: VerificarPerfilIncompletoUseCase,
    private val actualizarUsuarioUseCase: ActualizarUsuarioUseCase
) : ViewModel() {
    var usuarioSeleccionado by mutableStateOf<Usuario?>(null) // Para mostrar mi propio perfil
        private set
    var usuarioVisitado by mutableStateOf<Usuario?>(null) // Para mostrar el perfil de otro usuario
        private set
    var perfilEstaIncompleto by mutableStateOf(false)
        private set

    private val apiGeoref = GeorefApi.crear()
    var listaProvincias by mutableStateOf(emptyList<ProvinciaGeoref>())
    var listaLocalidades by mutableStateOf(emptyList<LocalidadGeoref>())
    var estaGuardandoPerfil by mutableStateOf(false)
        private set
    var errorApiGeoref by mutableStateOf<String?>(null)
        private set

    fun cargarUsuarioPorId(id: String, esMiPerfil: Boolean) {
        viewModelScope.launch {
            val resultado = obtenerUsuarioPorIdUseCase(id)

            if (esMiPerfil) {
                usuarioSeleccionado = resultado
                perfilEstaIncompleto = verificarPerfilIncompletoUseCase(usuarioSeleccionado)
            } else {
                usuarioVisitado = resultado
            }
        }
    }

    suspend fun obtenerUsuarioPorIdDirecto(id: String): Usuario? {
        return obtenerUsuarioPorIdUseCase(id)
    }

    fun limpiarUsuario() {
        usuarioSeleccionado = null
        perfilEstaIncompleto = false
    }

    fun actualizarDatosPerfil(
        contexto: android.content.Context,
        nombreNuevo: String,
        provinciaNueva: String,
        ciudadNueva: String,
        imagenNueva: String?,
        onExito: () -> Unit
    ) {
        val usuarioActual = usuarioSeleccionado ?: return

        estaGuardandoPerfil = true

        viewModelScope.launch {
            var lat = 0.0
            var lng = 0.0

            // Se buscan las coordenadas en un hilo separado
            withContext(Dispatchers.IO) {
                try {
                    val geocoder = android.location.Geocoder(contexto, java.util.Locale.getDefault())
                    val direccionBusqueda = "$ciudadNueva, $provinciaNueva, Argentina"
                    val direcciones = geocoder.getFromLocationName(direccionBusqueda, 1)

                    if (!direcciones.isNullOrEmpty()) {
                        lat = direcciones[0].latitude
                        lng = direcciones[0].longitude
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val urlFinal = if (imagenNueva != null && !imagenNueva.startsWith("http")) {
                subirFotoAImgBB(imagenNueva) ?: imagenNueva
            } else {
                imagenNueva
            }

            val usuarioActualizado = usuarioActual.copy(
                nombre = nombreNuevo.capitalizarPalabras(),
                provincia = provinciaNueva,
                ciudad = ciudadNueva,
                latitud = lat,
                longitud = lng,
                imagen = urlFinal
            )

            actualizarUsuarioUseCase(usuarioActualizado)

            usuarioSeleccionado = usuarioActualizado
            perfilEstaIncompleto = false

            estaGuardandoPerfil = false
            onExito()
        }
    }

    // Ubicacion
    fun cargarProvincias(hayInternet: Boolean) {
        if (!hayInternet) {
            errorApiGeoref = "No hay conexión a internet para cargar las provincias."
            return
        }

        errorApiGeoref = null

        viewModelScope.launch {
            try {
                val respuesta = apiGeoref.obtenerProvincias()
                listaProvincias = respuesta.provincias
            } catch (e: Exception) {
                errorApiGeoref = "No se pudieron cargar las provincias. Verifica tu conexión a internet."
            }
        }
    }

    fun cargarLocalidadesPorProvincia(provincia: ProvinciaGeoref, hayInternet: Boolean) {
        if (!hayInternet) {
            errorApiGeoref = "No hay conexión a internet para cargar las ciudades."
            return
        }

        errorApiGeoref = null

        viewModelScope.launch {
            try {
                val respuesta = apiGeoref.obtenerLocalidades(provincia.id)
                listaLocalidades = respuesta.localidades
            } catch (e: Exception) {
                errorApiGeoref = "No se pudieron cargar las ciudades. Verifica tu conexión a internet."
            }
        }
    }

    // Foto de perfil
    // Recibe una URI de una imagen, la guarda en el almacenamiento local y devuelve la ruta del nuevo archivo.
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

    fun String.capitalizarPalabras(): String {
        return this.lowercase().split(" ").joinToString(" ") {
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }
}