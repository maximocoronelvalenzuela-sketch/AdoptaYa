package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.data.servicios.NotificacionWorker
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.componentes.MascotaCard
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.NotificacionViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun PantallaFavoritos(
    favoritoViewModel: FavoritoViewModel,
    usuarioViewModel: UsuarioViewModel,
    authViewModel: AuthViewModel,
    notificacionViewModel: NotificacionViewModel,
    mascotaViewModel: MascotaViewModel,
    alClickearMascota: (String) -> Unit,
    alClickearInicio: () -> Unit,
    alClickearMapa: () -> Unit,
    alClickearPerfil: () -> Unit,
    alClickearPublicar: () -> Unit,
    alClickearNotificaciones: () -> Unit,
    alClickearPerfilDueño: (String) -> Unit
) {

    val mascotas = favoritoViewModel.favoritosDeUsuario
    val idActual = authViewModel.idUsuarioActual
    val alertaPerfil = usuarioViewModel.perfilEstaIncompleto
    val contexto = LocalContext.current

    LaunchedEffect(idActual) {
        if (idActual != null) {
            favoritoViewModel.cargarFavoritosDeUsuario(idActual)
            usuarioViewModel.cargarUsuarioPorId(idActual, true)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                titulo = "Favoritos",
                mostrarBotonNotificaciones = true,
                alClickearNotificaciones = { alClickearNotificaciones() }
            )
        },
        bottomBar = {
            BarraNavegacion(
                rutaActual = Pantallas.Favoritos.ruta,
                mostrarAlertaPerfil = alertaPerfil,
                alNavegar = { ruta ->
                    when (ruta) {
                        Pantallas.Mascotas.ruta -> alClickearInicio()
                        Pantallas.Mapa.ruta -> alClickearMapa()
                        Pantallas.Perfil.ruta -> alClickearPerfil()
                        Pantallas.Publicar.ruta -> alClickearPublicar()
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
        ){

            items(mascotas) { mascota ->
                val esFav = favoritoViewModel.esFavorito(mascota.id)
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
//                                        val datos = androidx.work.workDataOf(
//                                            "titulo" to "¡A alguien le gusta tu mascota!",
//                                            "descripcion" to mascota.nombre+" fue agregado a favoritos.",
//                                            "notificacionId" to idGenerado,
//                                            "idReceptor" to mascota.idUsuario
//                                        )
//                                        val peticion = androidx.work.OneTimeWorkRequestBuilder<NotificacionWorker>()
//                                            .setInputData(datos)
//                                            .setInitialDelay(40, java.util.concurrent.TimeUnit.SECONDS)
//                                            .build()
//                                        androidx.work.WorkManager.getInstance(contexto).enqueue(peticion)
                                    }
                                )
                            }
                        }
                    },
                    alClickearPerfilDueño = {
                        alClickearPerfilDueño(mascota.idUsuario)
                    },
                    esFavorito = favoritoViewModel.esFavorito(mascota.id)
                )
            }
        }
    }

}