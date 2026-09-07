package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.componentes.ModalRequiereLogin
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel
import com.example.adoptaya.util.hayConexionAInternet
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
    val contexto = LocalContext.current
    val sharedPrefs = contexto.getSharedPreferences("PreferenciasMapa", android.content.Context.MODE_PRIVATE)

    var mostrarModalLogin by remember { mutableStateOf(false) }
    val idActual = authViewModel.idUsuarioActual
    val alertaPerfil = usuarioViewModel.perfilEstaIncompleto

    var sinInternet by remember { mutableStateOf(false) }
    var mapaYaDescargado by remember {
        mutableStateOf(sharedPrefs.getBoolean("mapa_cargado", false))
    }

    LaunchedEffect(Unit) {
        val noHayRed = !hayConexionAInternet(contexto)
        sinInternet = noHayRed

        // Si hay internet, se guarda en memoria que el mapa ya se cargó al menos una vez
        if (!noHayRed && !mapaYaDescargado) {
            sharedPrefs.edit().putBoolean("mapa_cargado", true).apply()
            mapaYaDescargado = true
        }
    }

    val santiago = LatLng(-27.7951, -64.2615)
    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(santiago, 12f)
        }
    val usuario = usuarioViewModel.usuarioSeleccionado

    LaunchedEffect(idActual) {
        mascotaViewModel.cargarMascotas()

        if (idActual != null) {
            usuarioViewModel.cargarUsuarioPorId(idActual, true)
        }
    }

    LaunchedEffect(usuario?.latitud, usuario?.longitud) {
        if (usuario != null && usuario.latitud != 0.0 && usuario.longitud != 0.0) {
            val ciudadUsuario = LatLng(usuario.latitud, usuario.longitud)

            cameraPositionState.animate(
                update = com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(ciudadUsuario, 12f),
                durationMs = 1500 // 1,5 segundos
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            ) {
                val mascotasVisibles =
                    mascotas.filter { it.estado == Enums.EstadoMascota.DISPONIBLE }
                mascotasVisibles.forEach { mascota ->

                    Marker(
                        state = MarkerState(
                            position = LatLng(mascota.latitud, mascota.longitud)
                        ),
                        onClick = {
                            alClickearMascota(mascota.id)
                            true
                        }
                    )
                }
            }

            // Sin internet y nunca cargó el mapa
            if (sinInternet && !mapaYaDescargado) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Sin mapa",
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Conectate a internet para ver el mapa.",
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            } else if (sinInternet && mapaYaDescargado) { // Sin internet y ya cargó el mapa
                Surface(
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
                ) {
                    Text(
                        text = "Modo sin conexión: Solo se verán las áreas guardadas previamente.",
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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