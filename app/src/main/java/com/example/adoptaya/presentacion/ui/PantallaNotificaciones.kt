package com.example.adoptaya.presentacion.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.presentacion.viewmodel.NotificacionViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNotificaciones(
    notificacionViewModel: NotificacionViewModel,
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel,
    notificacionIdInicial: String? = null,
    alVolver: () -> Unit,
    alClickearMascota: (String) -> Unit,
    alClickearPerfil: (String) -> Unit
) {
    val notificaciones = notificacionViewModel.notificacionesDeUsuario
    val idActual = authViewModel.idUsuarioActual

    // Estado para controlar qué notificación mostrar en el modal (null = cerrado)
    var notificacionSeleccionada by remember { mutableStateOf<Notificacion?>(null) }

    // Estado para controlar si el deep link ya fue consumido
    var deepLinkConsumido by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(idActual) {
        if (idActual != null) {
            notificacionViewModel.cargarNotificacionesDeUsuario(idActual)
        }
    }

    // Efecto para abrir el modal automáticamente si llegamos desde el toque de la notificación
    LaunchedEffect(notificaciones, notificacionIdInicial) {
        if (notificacionIdInicial != null && notificaciones.isNotEmpty() && notificacionSeleccionada == null && !deepLinkConsumido) {
            val notiEncontrada = notificaciones.find { it.id == notificacionIdInicial }
            if (notiEncontrada != null) {
                notificacionSeleccionada = notiEncontrada
                deepLinkConsumido = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                titulo = "Notificaciones",
                mostrarBotonVolver = true,
                alVolver = { alVolver() }
            )
        }
    ) { paddingValues ->

        Column(modifier = Modifier
            .padding(paddingValues)
            .background(Color(0xFFF3F3F3))
        ) {

            // Filtros rapidos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipFiltro(texto = "Todas", seleccionado = true)
                ChipFiltro(texto = "Solicitudes", seleccionado = false)
                ChipFiltro(texto = "Mascotas", seleccionado = false)
            }


            // Lista de Notificaciones
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "HOY",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 8.dp))
                }

                items(notificaciones) { noti ->
                    ItemNotificacion(
                        notificacion = noti,
                        alClickear = { notificacionSeleccionada = noti },

                    )
                }
            }
        }


        // El modal de detalle (se muestra solo si hay una seleccionada)
        notificacionSeleccionada?.let { noti ->
            val emisor = usuarioViewModel.usuarioVisitado
            LaunchedEffect(notificacionSeleccionada!!.idUsuarioEmisor) {
                usuarioViewModel.cargarUsuarioPorId(notificacionSeleccionada!!.idUsuarioEmisor, false)
            }

            DialogoDetalleNotificacion(
                notificacion = noti,
                telefonoEmisor = emisor?.telefono,
                onDismiss = {
                    notificacionSeleccionada = null
                    usuarioViewModel.limpiarUsuario()
                },
                onNavegarAMascota = { id ->
                    alClickearMascota(id)
                    notificacionSeleccionada = null
                },
                onNavegarAPerfil = { id ->
                    alClickearPerfil(id)
                    notificacionSeleccionada = null
                }
            )
        }
    }
}


// Componentes de la misma pantalla (modularidad)
@Composable
fun ChipFiltro(texto: String, seleccionado: Boolean) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (seleccionado) Color(0xFFE85A13) else Color.White,
        border = if (!seleccionado) BorderStroke(1.dp, Color(0xFFEEEEEE)) else null
    ) {
        Text(
            text = texto,
            color = if (seleccionado) Color.White else Color.Gray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ItemNotificacion(notificacion: Notificacion, alClickear: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { alClickear() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                shape = CircleShape,
                color = Color(0xFFFFF3E0),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFFE85A13),
                    modifier = Modifier.padding(12.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Textos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notificacion.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = notificacion.descripcion,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hace 10 min", // Hardcodeado por ahora
                    fontSize = 12.sp,
                    color = Color(0xFFE85A13)
                )
            }

            // Puntito de "No leida"
            if (!notificacion.leida) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFFE85A13), shape = CircleShape)
                        .align(Alignment.Top)
                )
            }
        }
    }
}

// El modal de detalle
@Composable
fun DialogoDetalleNotificacion(
    notificacion: Notificacion,
    telefonoEmisor: String?,
    onDismiss: () -> Unit,
    onNavegarAMascota: (String) -> Unit,
    onNavegarAPerfil: (String) -> Unit
) {
    val contexto = LocalContext.current

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Boton de cerrar (X) arriba a la derecha
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                    }
                }

                // Foto de perfil grande
                Surface(
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier.size(80.dp)
                ) {
                    // TODO: Foto perfil real del usuario
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(16.dp), tint = Color.DarkGray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre y Mensaje
                Text("Juan Perez", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF6ED),  // Naranja tenue
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notificacion.descripcion,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logica de botones segun el tipo
                when (notificacion.tipo) {
                    Enums.TipoNotificacion.FAVORITO -> {
                        BotonPrimario(texto = "Ver mi Mascota", icono = Icons.Default.Pets) {
                            notificacion.idMascota?.let { onNavegarAMascota(it) }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        BotonSecundario(texto = "Ver Perfil del interesado", icono = Icons.Default.Person) {
                            notificacion.idUsuarioEmisor.let { onNavegarAPerfil(it) }
                        }
                    }
                    Enums.TipoNotificacion.NUEVA_MASCOTA -> {
                        BotonPrimario(texto = "Ver nueva Mascota", icono = Icons.Default.Search) {
                            notificacion.idMascota?.let { onNavegarAMascota(it) }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        BotonSecundario(texto = "Ver Perfil del dueño", icono = Icons.Default.Person) {
                            notificacion.idUsuarioEmisor.let { onNavegarAPerfil(it) }
                        }
                    }
                    Enums.TipoNotificacion.CONTACTO -> {
                        BotonPrimario(
                            texto = "WhatsApp de contacto",
                            icono = Icons.AutoMirrored.Default.Chat,
                            habilitado = telefonoEmisor != null
                        ) {
                            if (telefonoEmisor != null) {
                                try {
                                    val mensajeBase = "Hola, vi tu solicitud en AdoptaYa. ¿Sigues interesado/a en la adopción?"
                                    // Se codifica el texto para que los espacios y signos pasen bien por la URL
                                    val mensajeCodificado = java.net.URLEncoder.encode(mensajeBase, "UTF-8")

                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://wa.me/$telefonoEmisor?text=$mensajeCodificado")
                                    }
                                    contexto.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(contexto, "No se pudo abrir el enlace", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        BotonSecundario(texto = "Ver Mascota", icono = Icons.Default.Pets) {
                            notificacion.idMascota?.let { onNavegarAMascota(it) }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        BotonSecundario(texto = "Ver Perfil de interesado", icono = Icons.Default.Person) {
                            notificacion.idUsuarioEmisor.let { onNavegarAPerfil(it) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Footer de tiempo (TODO: hardcodeado)
                Text("RECIBIDO HOY A LAS 10:45 AM", color = Color.LightGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Estilos de botones
@Composable
fun BotonPrimario(texto: String, icono: ImageVector, habilitado: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE85A13),
            contentColor = Color.White,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.DarkGray
        ),
        shape = RoundedCornerShape(25.dp)
    ) {
        Icon(icono, contentDescription = null); Spacer(modifier = Modifier.width(8.dp))
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BotonSecundario(texto: String, icono: ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE85A13)),
        border = BorderStroke(1.dp, Color(0xFFE85A13))
    ) {
        Icon(icono, contentDescription = null); Spacer(modifier = Modifier.width(8.dp))
        Text(texto, fontWeight = FontWeight.Bold)
    }
}