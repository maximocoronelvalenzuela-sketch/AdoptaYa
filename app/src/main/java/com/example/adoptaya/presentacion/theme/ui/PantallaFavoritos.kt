package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.componentes.MascotaCard
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel

@Composable
fun PantallaFavoritos(
    favoritoViewModel: FavoritoViewModel,
    alClickearMascota: (String) -> Unit,
    alClickearInicio: () -> Unit,
    alClickearMapa: () -> Unit,
    alClickearPerfil: () -> Unit,
    alClickearPublicar: () -> Unit
) {

    val mascotas =
        favoritoViewModel.favoritosDeUsuario

    LaunchedEffect(Unit) {
        // Hardcodeado temporalmente porque todavia no hay sesion
        favoritoViewModel.cargarFavoritosDeUsuario("1")
    }

    Scaffold(
        bottomBar = {
            BarraNavegacion(
                rutaActual = Pantallas.Favoritos.ruta,
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
        ){

            items(mascotas) { mascota ->

                MascotaCard(
                    mascota = mascota,
                    alClickear = {
                        alClickearMascota(mascota.id)
                    }
                )
            }
        }
    }

}