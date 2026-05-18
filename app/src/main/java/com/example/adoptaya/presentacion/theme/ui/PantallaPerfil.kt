package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun PantallaPerfil(
    usuarioViewModel: UsuarioViewModel
) {

    val usuario = usuarioViewModel.usuarioSeleccionado

    LaunchedEffect(Unit) {

        // Hardcodeado temporalmente porque todavia no hay sesion
        usuarioViewModel.cargarUsuarioPorId("1")
    }

    usuario?.let {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp)
        ) {

            Text(
                text = it.nombre
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = it.email
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}