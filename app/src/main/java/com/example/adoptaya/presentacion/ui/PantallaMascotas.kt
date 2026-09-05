package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.data.servicios.NotificacionWorker
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.componentes.MascotaCard
import com.example.adoptaya.presentacion.componentes.ModalRequiereLogin
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.NotificacionViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel
import com.example.adoptaya.util.hayConexionAInternet

@Composable
fun PantallaMascotas(
    mascotaViewModel: MascotaViewModel,
    favoritoViewModel: FavoritoViewModel,
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel,
    notificacionViewModel: NotificacionViewModel,
    alClickearMascota: (String) -> Unit,
    alClickearFavorito: () -> Unit,
    alClickearPerfil: () -> Unit,
    alClickearMapa: () -> Unit,
    alClickearPublicar: () -> Unit,
    alClickearNotificaciones: () -> Unit,
    alClickearPerfilDueño: (String) -> Unit,
    alNavegarLogin: () -> Unit
) {

    val mascotas = mascotaViewModel.mascotas
    val contexto = LocalContext.current

    var mostrarModalLogin by remember { mutableStateOf(false) }
    val idActual = authViewModel.idUsuarioActual
    val alertaPerfil = usuarioViewModel.perfilEstaIncompleto

    LaunchedEffect(idActual) {
        mascotaViewModel.cargarMascotas()

        if (idActual != null) {
            usuarioViewModel.cargarUsuarioPorId(idActual, true)
        } else {
            usuarioViewModel.limpiarUsuario()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                titulo = "AdoptaYa",
                color = Color(0xFFE85A13),
                tamañoFuente = 28.sp,
                mostrarBotonNotificaciones = true,
                alClickearNotificaciones = {
                    if(idActual != null) alClickearNotificaciones() else mostrarModalLogin = true
                },
                mostrarBotonLogin = idActual == null,
                alClickearLogin = { alNavegarLogin() }
            )
        },
        bottomBar = {
            BarraNavegacion(
                rutaActual = Pantallas.Mascotas.ruta,
                mostrarAlertaPerfil = alertaPerfil,
                alNavegar = { ruta ->
                    when (ruta) {
                        Pantallas.Favoritos.ruta -> {
                            if(idActual != null) alClickearFavorito() else mostrarModalLogin = true
                        }
                        Pantallas.Perfil.ruta -> {
                            if (idActual != null) alClickearPerfil() else mostrarModalLogin = true
                        }
                        Pantallas.Mapa.ruta -> alClickearMapa()
                        Pantallas.Publicar.ruta -> {
                            if (idActual != null) alClickearPublicar() else mostrarModalLogin = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .background(Color(0xFFF3F3F3))
        ) {
            items(mascotas) { mascota ->
                val esFav = favoritoViewModel.esFavorito(mascota.id)
                // produceState maneja la corrutina en segundo plano e inserta el valor a la variable (dueño)
                val dueño by produceState<Usuario?>(initialValue = null, key1 = mascota.idUsuario) {
                    value = usuarioViewModel.obtenerUsuarioPorIdDirecto(mascota.idUsuario)
                }
                val tiempoTranscurrido = mascotaViewModel.obtenerTiempoTranscurrido(mascota.fechaHoraAlta)

                MascotaCard(
                    mascota = mascota,
                    nombreDueño = dueño?.nombre ?: "Cargando...",
                    fotoDueño = dueño?.imagen,
                    tiempoTranscurrido = tiempoTranscurrido,
                    alClickearMascota = {
                        alClickearMascota(mascota.id)
                    },
                    alClickearPerfilDueño = {
                        alClickearPerfilDueño(mascota.idUsuario)
                    },
                    alClickearFavorito = {
                        if (idActual != null) {
                            favoritoViewModel.toggleFavorito(idActual, mascota)

                            if (!esFav && idActual != mascota.idUsuario) {
                                notificacionViewModel.enviarNotificacionFavorito(
                                    idEmisor = idActual,
                                    idReceptor = mascota.idUsuario,
                                    idMascota = mascota.id,
                                    nombreMascota = mascota.nombre,
                                    imagenMascota = mascota.imagenes.firstOrNull(),
                                    onSuccess = { idGenerado ->
                                        val datos = androidx.work.workDataOf(
                                            "titulo" to "¡A alguien le gusta tu mascota!",
                                            "descripcion" to mascota.nombre+" fue agregado a favoritos.",
                                            "notificacionId" to idGenerado,
                                            "idReceptor" to mascota.idUsuario
                                        )
                                        val peticion = androidx.work.OneTimeWorkRequestBuilder<NotificacionWorker>()
                                            .setInputData(datos)
                                            .setInitialDelay(40, java.util.concurrent.TimeUnit.SECONDS)
                                            .build()
                                        androidx.work.WorkManager.getInstance(contexto).enqueue(peticion)
                                    }
                                )
                            }
                        } else {
                            // Es un usuario Invitado
                            mostrarModalLogin = true
                        }
                    },
                    esFavorito = esFav
                )
            }
        }

        if (mostrarModalLogin) {
            ModalRequiereLogin(
                onDismiss = { mostrarModalLogin = false },
                onConfirmar = {
                    mostrarModalLogin = false
                    alNavegarLogin() // Redirige al Login
                }
            )
        }
    }
}