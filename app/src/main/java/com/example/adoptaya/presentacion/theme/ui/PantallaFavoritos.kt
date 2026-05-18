package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel

@Composable
fun PantallaFavoritos(
    favoritoViewModel: FavoritoViewModel,
    alClickearMascota: (String) -> Unit
) {

    val mascotas =
        favoritoViewModel.favoritosDeUsuario

    LaunchedEffect(Unit) {
        // Hardcodeado temporalmente porque todavia no hay sesion
        favoritoViewModel.cargarFavoritosDeUsuario("1")
    }

    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
    ){

        items(mascotas) { mascota ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {

                        alClickearMascota(mascota.id)
                    }
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(mascota.nombre)
                    Text(mascota.tipo)
                    Text(mascota.descripcionPersonalidad)
                }
            }
        }
    }
}