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
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.NotificacionViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun PantallaDePrueba(
    mascotaViewmodel: MascotaViewModel,
    usuarioViewModel: UsuarioViewModel,
    favoritoViewModel: FavoritoViewModel,
    notificacionViewModel: NotificacionViewModel
) {
    val mascotas = mascotaViewmodel.mascotas
    val mascotaSeleccionada = mascotaViewmodel.mascotaSeleccionada
    val usuarioSeleccionado = usuarioViewModel.usuarioSeleccionado
    val favoritosDeUsuario = favoritoViewModel.favoritosDeUsuario
    val notificacionesDeUsuario = notificacionViewModel.notificacionesDeUsuario

    var inputIdMascota by remember { mutableStateOf("") }
    var inputIdUsuario by remember { mutableStateOf("") }
    var inputIdUsuarioParaFavoritos by remember { mutableStateOf("") }
    var inputIdUsuarioParaNotificaciones by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Prueba de entidad Mascota
        Button (
            onClick = {mascotaViewmodel.cargarMascotas()}
        ) {
            Text("Cargar mascotas")
        }

        mascotas.forEach {
            Text("Mascota: " + it.nombre)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prueba de entidad Usuario
        OutlinedTextField(
            value = inputIdMascota,
            onValueChange = {inputIdMascota = it},
            label = {Text("ID de la mascota")}
        )

        Button(
            onClick = {mascotaViewmodel.cargarMascotaPorId(inputIdMascota)}
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

        Spacer(modifier = Modifier.height(16.dp))

        // Prueba de entidad Usuario
        OutlinedTextField(
            value = inputIdUsuario,
            onValueChange = {inputIdUsuario = it},
            label = {Text("ID del usuario")}
        )

        Button(
            onClick = {usuarioViewModel.cargarUsuarioPorId(inputIdUsuario)}
        ){
            Text("Buscar por ID")
        }

        if (usuarioSeleccionado != null) {
            Text("Resultado:")
            Text("Usuario seleccionado: " + usuarioSeleccionado.nombre)
            Text("Email: " + usuarioSeleccionado.email)
        }
        else{
            Text("No se ha seleccionado ningun usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prueba de entidad Favorito
        OutlinedTextField(
            value = inputIdUsuarioParaFavoritos,
            onValueChange = {inputIdUsuarioParaFavoritos = it},
            label = {Text("ID del usuario para favoritos")}
        )

        Button(
            onClick = {favoritoViewModel.cargarFavoritosDeUsuario(inputIdUsuarioParaFavoritos)}
        ){
            Text("Buscar favoritos por usuario")
        }

        if (favoritosDeUsuario.isNotEmpty()) {
            Text("Resultado:")
            favoritosDeUsuario.forEach {
                Text("Favorito: " + it.nombre)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prueba de entidad Notificacion
        OutlinedTextField(
            value = inputIdUsuarioParaNotificaciones,
            onValueChange = {inputIdUsuarioParaNotificaciones = it},
            label = {Text("ID del usuario para notificaciones")}
        )

        Button(
            onClick = {notificacionViewModel.cargarNotificacionesDeUsuario(inputIdUsuarioParaNotificaciones)}
        ){
            Text("Buscar notificaciones por usuario")
        }

        if (notificacionesDeUsuario.isNotEmpty()) {
            Text("Resultado:")
            notificacionesDeUsuario.forEach {
                Text("----------------------------")
                Text("Notificacion: " + it.titulo)
                Text("Notificacion: " + it.descripcion)
            }
        }
    }
}