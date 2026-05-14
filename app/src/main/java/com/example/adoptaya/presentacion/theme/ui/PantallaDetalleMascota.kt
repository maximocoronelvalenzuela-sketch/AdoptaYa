package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel

@Composable
fun PantallaDetalleMascota(
    mascotaId: String,
    mascotaViewModel: MascotaViewModel
) {

    val mascota = mascotaViewModel.mascotaSeleccionada

    LaunchedEffect(Unit) {

        mascotaViewModel.cargarMascotaPorId(mascotaId)
    }

    mascota?.let {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp)
        ) {

            Text(text = it.nombre)
            Text(text = it.tipo)
            Text(text = it.descripcionPersonalidad)
            Text(text = it.descripcionAdicional)
            Text(text = "Edad: " + it.edad)
        }
    }
}