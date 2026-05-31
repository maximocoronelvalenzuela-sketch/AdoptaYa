package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun PantallaMapa(
    mascotaViewModel: MascotaViewModel,
    alClickearMascota: (String) -> Unit,
    alClickearInicio: () -> Unit,
    alClickearFavorito: () -> Unit,
    alClickearPerfil: () -> Unit,
    alClickearPublicar: () -> Unit
) {

    val mascotas = mascotaViewModel.mascotas

    LaunchedEffect(Unit) {
        mascotaViewModel.cargarMascotas()
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
        bottomBar = {
            BarraNavegacion(
                rutaActual = Pantallas.Mapa.ruta,
                alNavegar = { ruta ->
                    when (ruta) {
                        Pantallas.Mascotas.ruta -> alClickearInicio()
                        Pantallas.Favoritos.ruta -> alClickearFavorito()
                        Pantallas.Perfil.ruta -> alClickearPerfil()
                        Pantallas.Publicar.ruta -> alClickearPublicar()
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

            mascotas.forEach { mascota ->

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
    }
}