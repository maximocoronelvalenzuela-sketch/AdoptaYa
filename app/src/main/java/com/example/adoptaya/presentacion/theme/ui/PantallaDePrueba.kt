package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel

@Composable
fun PantallaDePrueba(viewmodel: MascotaViewModel) {
    val mascotas = viewmodel.mascotas
    val mascotaSeleccionada = viewmodel.mascotaSeleccionada

    var inputId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding() // <--- ESTA ES LA CLAVE
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {


        Button (
            onClick = {viewmodel.cargarMascotas()}
        ) {
            Text("Cargar mascotas")
        }

        mascotas.forEach {
            Text("Mascota: " + it.nombre)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputId,
            onValueChange = {inputId = it},
            label = {Text("ID de la mascota")}
        )

        Button(
            onClick = {viewmodel.cargarMascotaPorId(inputId)}
        ){
            Text("Buscar por ID")
        }

        if (mascotaSeleccionada != null) {
                Text("Resultado:")
                Text("Mascota seleccionada: " + mascotaSeleccionada.nombre)
                Text("Edad: " + mascotaSeleccionada.edad)
        }
        else{
            Text("No se ha seleccionado ninguna mascota")
        }
    }
}