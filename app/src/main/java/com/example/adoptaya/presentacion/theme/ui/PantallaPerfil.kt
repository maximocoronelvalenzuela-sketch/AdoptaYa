package com.example.adoptaya.presentacion.theme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    alClickearNotificaciones: () -> Unit,
    alVolver: () -> Unit
) {

    val usuario = usuarioViewModel.usuarioSeleccionado

    // TODO: ID del usuario logueado cuando se implemente login real
    val idUsuarioLogueado = "1"
    val esMiPerfil = usuarioId == idUsuarioLogueado
    LaunchedEffect(usuarioId) {
        usuarioViewModel.cargarUsuarioPorId(usuarioId)
    }

    Scaffold(
        topBar = {
            TopBar(
                titulo = if (esMiPerfil) "Mi Perfil" else "Perfil de "+usuario?.nombre,
                mostrarBotonNotificaciones = esMiPerfil,    // Si es mi perfil, veo el boton de notificaciones
                alClickearNotificaciones = { alClickearNotificaciones() },
                mostrarBotonVolver = !esMiPerfil,   // Si no es mi perfil, veo el boton de volver
                alVolver = { alVolver() }
            )
        },
        bottomBar = {
            if (esMiPerfil) {   // Solo muestro el bottomBar si es mi perfil
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
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        if (usuario == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE85A13))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .background(Color(0xFFF3F3F3)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Foto y datos personales
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFF3E0),
                    modifier = Modifier.size(120.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        tint = Color(0xFFE85A13),
                        modifier = Modifier.padding(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = usuario.nombre,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color(0xFF1A1A1A)
                )

                Text(
                    text = "Barrio",    // TODO: Barrio y Provincia
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botones
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (esMiPerfil) {
                        Button(
                            onClick = { /* TODO: Editar perfil */ },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE85A13)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Editar Perfil", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { /* TODO: Cerrar sesión */ },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Salir", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { /* TODO: WhatsApp */ },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE85A13)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Contactar", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Seccion de publicaciones
                Surface(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = if (esMiPerfil) "Mis publicaciones" else "Publicaciones de "+usuario.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // TODO: Mascotas del usuario
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(Color.LightGray,
                            RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay publicaciones recientes", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}