package com.example.adoptaya.presentacion.componentes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    titulo: String,
    color: Color = Color.Unspecified,
    tamañoFuente: TextUnit = 22.sp
) {

    TopAppBar(
        title = {
            Text(
                text = titulo,
                color = color,
                fontSize = tamañoFuente
            )
        }
    )
}