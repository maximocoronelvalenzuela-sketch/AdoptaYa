package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun PantallaPerfil(
    usuarioId: String,
    usuarioViewModel: UsuarioViewModel,
    alClickearInicio: () -> Unit,
    alClickearFavorito: () -> Unit,
    alClickearMapa: () -> Unit,
    alClickearPublicar: () -> Unit,
    alClickearNotificaciones: () -> Unit
) {

    val usuario = usuarioViewModel.usuarioSeleccionado

    LaunchedEffect(usuarioId) {
        usuarioViewModel.cargarUsuarioPorId(usuarioId)
    }

    Scaffold(
        topBar = {
            TopBar(
                titulo = "Perfil",
                mostrarBotonNotificaciones = true,
                alClickearNotificaciones = { alClickearNotificaciones() }
            )
        },
        bottomBar = {
            BarraNavegacion(
                rutaActual = Pantallas.Perfil.ruta,
                alNavegar = { ruta ->
                    when (ruta) {
                        Pantallas.Mascotas.ruta -> alClickearInicio()
                        Pantallas.Favoritos.ruta -> alClickearFavorito()
                        Pantallas.Mapa.ruta -> alClickearMapa()
                        Pantallas.Publicar.ruta -> alClickearPublicar()
                    }
                }
            )
        }
    ) { paddingValues ->
        usuario?.let {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(Color(0xFFF3F3F3))
            ) {
                Text(it.nombre)
                Spacer(modifier = Modifier.height(8.dp))

                Text(it.email)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}