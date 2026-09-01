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
import com.google.android.play.integrity.internal.ac
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuarioViewModel (
    private val obtenerUsuarioPorIdUseCase: ObtenerUsuarioPorIdUseCase,
    private val verificarPerfilIncompletoUseCase: VerificarPerfilIncompletoUseCase,
    private val actualizarUsuarioUseCase: ActualizarUsuarioUseCase
) : ViewModel() {
    var usuarioSeleccionado by mutableStateOf<Usuario?>(null)
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

    init {
        cargarProvincias()
    }

    fun cargarUsuarioPorId(id: String) {
        viewModelScope.launch {
            usuarioSeleccionado = obtenerUsuarioPorIdUseCase(id)

            perfilEstaIncompleto = verificarPerfilIncompletoUseCase(usuarioSeleccionado)
        }
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

            val usuarioActualizado = usuarioActual.copy(
                nombre = nombreNuevo,
                provincia = provinciaNueva,
                ciudad = ciudadNueva,
                latitud = lat,
                longitud = lng,
                imagen = imagenNueva
            )

            actualizarUsuarioUseCase(usuarioActualizado)

            usuarioSeleccionado = usuarioActualizado
            perfilEstaIncompleto = false

            estaGuardandoPerfil = false
            onExito()
        }
    }

    // Ubicacion
    private fun cargarProvincias() {
        viewModelScope.launch {
            try {
                val respuesta = apiGeoref.obtenerProvincias()
                listaProvincias = respuesta.provincias
            } catch (e: Exception) {
                errorApiGeoref = "No se pudieron cargar las provincias. Verifica tu conexión a internet."
            }
        }
    }

    fun cargarLocalidadesPorProvincia(provincia: ProvinciaGeoref) {
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
        val inputStream = context.contentResolver.openInputStream(uri)
        val archivoLocal = java.io.File(context.filesDir, "perfil_${java.util.UUID.randomUUID()}.jpg")
        val outputStream = java.io.FileOutputStream(archivoLocal)

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        return archivoLocal.absolutePath
    }
}