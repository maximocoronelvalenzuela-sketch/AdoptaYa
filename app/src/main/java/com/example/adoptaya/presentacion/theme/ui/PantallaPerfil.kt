package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun PantallaPerfil(
    usuarioViewModel: UsuarioViewModel,
    alClickearInicio: () -> Unit,
    alClickearFavorito: () -> Unit,
    alClickearMapa: () -> Unit,
    alClickearPublicar: () -> Unit
) {

    val usuario = usuarioViewModel.usuarioSeleccionado

    LaunchedEffect(Unit) {

        // Hardcodeado temporalmente porque todavia no hay sesion
        usuarioViewModel.cargarUsuarioPorId("1")
    }

    Scaffold(
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
            ) {
                Text(it.nombre)
                Spacer(modifier = Modifier.height(8.dp))

                Text(it.email)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}