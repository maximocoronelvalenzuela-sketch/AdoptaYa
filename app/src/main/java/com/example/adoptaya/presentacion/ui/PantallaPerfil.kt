package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.adoptaya.presentacion.componentes.BarraNavegacion
import com.example.adoptaya.presentacion.componentes.MascotaCard
import com.example.adoptaya.presentacion.componentes.ModalRequiereLogin
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.navegacion.Pantallas
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun PantallaPerfil(
    usuarioId: String,
    usuarioViewModel: UsuarioViewModel,
    mascotaViewModel: MascotaViewModel,
    favoritoViewModel: FavoritoViewModel,
    authViewModel: AuthViewModel,
    alClickearInicio: () -> Unit,
    alClickearFavorito: () -> Unit,
    alClickearMapa: () -> Unit,
    alClickearPublicar: () -> Unit,
    alClickearNotificaciones: () -> Unit,
    alVolver: () -> Unit,
    alCerrarSesion: () -> Unit,
    alClickearMascota: (String) -> Unit,
    alNavegarLogin: () -> Unit,
    alEditarPerfil: () -> Unit
) {

    val usuario = usuarioViewModel.usuarioSeleccionado
    val alertaPerfil = usuarioViewModel.perfilEstaIncompleto
    val contexto = LocalContext.current
    val esMiPerfil = usuarioId == authViewModel.idUsuarioActual
    var mostrarModalLogin by remember { mutableStateOf(false) }

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
                    mostrarAlertaPerfil = alertaPerfil,
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
        if (usuario == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE85A13))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF3F3F3)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (esMiPerfil && alertaPerfil) {
                    item {
                        Surface(
                            color = Color(0xFFFFF3CD),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .padding(top = 24.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = "Perfil incompleto",
                                    tint = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Tu perfil está incompleto. Te recomendamos sumar tu ubicación y foto para generar más confianza en la comunidad.",
                                    color = Color.DarkGray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))

                    // Foto y datos personales
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFF3E0),
                        modifier = Modifier.size(120.dp)
                    ) {
                        if (usuario.imagen != null) {
                            AsyncImage(
                                model = usuario.imagen,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(24.dp), tint = Color.DarkGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = usuario.nombre,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = Color(0xFF1A1A1A)
                    )

                    Text(
                        text = usuario.ciudad + ", " + usuario.provincia,
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
                                onClick = { alEditarPerfil() },
                                modifier = Modifier.weight(1f).height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE85A13)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar Perfil", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    authViewModel.cerrarSesion(
                                        onExito = {
                                            android.widget.Toast.makeText(
                                                contexto,
                                                "Sesión cerrada",
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                            alCerrarSesion()
                                        }
                                    )
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                border = BorderStroke(1.dp, Color.Red)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Salir", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    // Seccion de publicaciones
                    Surface(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = if (esMiPerfil) "Mis publicaciones" else "Publicaciones de " + usuario.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                // Mascotas del usuario
                val mascotasDelUsuario = mascotaViewModel.mascotas.filter { it.idUsuario == usuarioId }

                if (mascotasDelUsuario.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                                .height(150.dp)
                                .background(Color.LightGray, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay publicaciones recientes", color = Color.Gray)
                        }
                    }
                } else {
                    items(mascotasDelUsuario) { mascota ->
                        val esFav = favoritoViewModel.esFavorito(mascota.id)

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                                MascotaCard(
                                    mascota = mascota,
                                    alClickearMascota = { alClickearMascota(mascota.id) },
                                    alClickearPerfilDueño = { },
                                    alClickearFavorito = {
                                        val idActual = authViewModel.idUsuarioActual
                                        if (idActual != null) {
                                            favoritoViewModel.toggleFavorito(idActual, mascota)
                                        } else {
                                            mostrarModalLogin = true
                                        }
                                    },
                                    esFavorito = esFav
                                )
                            }
                        }
                    }
                    item {
                        Surface(modifier = Modifier.fillMaxWidth().height(40.dp), color = Color.White) {}
                    }
                }
            }
        }

        if (mostrarModalLogin) {
            ModalRequiereLogin(
                onDismiss = { mostrarModalLogin = false },
                onConfirmar = {
                    mostrarModalLogin = false
                    alNavegarLogin()
                }
            )
        }
    }
}