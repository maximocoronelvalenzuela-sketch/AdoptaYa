package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.componentes.MascotaCard
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun PantallaFavoritos(
    favoritoViewModel: FavoritoViewModel,
    usuarioViewModel: UsuarioViewModel,
    authViewModel: AuthViewModel,
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

                MascotaCard(
                    mascota = mascota,
                    alClickearMascota = {
                        alClickearMascota(mascota.id)
                    },
                    alClickearFavorito = {
                        if (idActual != null) {
                            favoritoViewModel.toggleFavorito(idActual, mascota)
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