package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.componentes.ModalRequiereLogin
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun PantallaMapa(
    mascotaViewModel: MascotaViewModel,
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel,
    alClickearMascota: (String) -> Unit,
    alClickearInicio: () -> Unit,
    alClickearFavorito: () -> Unit,
    alClickearPerfil: () -> Unit,
    alClickearPublicar: () -> Unit,
    alClickearNotificaciones: () -> Unit,
    alNavegarLogin: () -> Unit
) {

    val mascotas = mascotaViewModel.mascotas

    var mostrarModalLogin by remember { mutableStateOf(false) }
    val idActual = authViewModel.idUsuarioActual
    val alertaPerfil = usuarioViewModel.perfilEstaIncompleto

    LaunchedEffect(idActual) {
        mascotaViewModel.cargarMascotas()

        if (idActual != null) {
            usuarioViewModel.cargarUsuarioPorId(idActual, true)
        }
    }

    val santiago = LatLng(
        -27.7951,
        -64.2615
    )

    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                santiago,
                12f
            )
        }

    Scaffold(
        topBar = {
            TopBar(
                titulo = "Mapa",
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
                rutaActual = Pantallas.Mapa.ruta,
                mostrarAlertaPerfil = alertaPerfil,
                alNavegar = { ruta ->
                    when (ruta) {
                        Pantallas.Mascotas.ruta -> alClickearInicio()
                        Pantallas.Favoritos.ruta -> {
                            if(idActual != null) alClickearFavorito() else mostrarModalLogin = true
                        }
                        Pantallas.Perfil.ruta -> {
                            if (idActual != null) alClickearPerfil() else mostrarModalLogin = true
                        }
                        Pantallas.Publicar.ruta -> {
                            if (idActual != null) alClickearPublicar() else mostrarModalLogin = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState,
        ) {
            val mascotasVisibles = mascotas.filter { it.estado == Enums.EstadoMascota.DISPONIBLE }
            mascotasVisibles.forEach { mascota ->

                Marker(
                    state = MarkerState(
                        position = LatLng(
                            mascota.latitud,
                            mascota.longitud
                        )
                    ),
                    onClick = {
                        alClickearMascota(mascota.id)

                        true
                    }
                )
            }
        }

        if (mostrarModalLogin) {
            ModalRequiereLogin(
                onDismiss = { mostrarModalLogin = false },
                onConfirmar = {
                    mostrarModalLogin = false
                    alNavegarLogin()
                }
            )
        }
    }
}