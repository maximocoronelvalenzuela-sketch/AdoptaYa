package com.example.adoptaya.presentacion.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.core.content.ContextCompat.checkSelfPermission
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
    //val notificaciones = notificacionViewModel.notificacionesDeUsuario
    val notificaciones = notificacionViewModel.notificacionesFiltradas
    val idActual = authViewModel.idUsuarioActual

    // Estado para controlar qué notificación mostrar en el modal (null = cerrado)
    var notificacionSeleccionada by remember { mutableStateOf<Notificacion?>(null) }

    // Estado para controlar si el deep link ya fue consumido
    var deepLinkConsumido by rememberSaveable { mutableStateOf(false) }

    val contexto = LocalContext.current
    var tienePermisoNotis by remember { mutableStateOf(true) }

    LaunchedEffect(idActual) {
        if (idActual != null) {
            notificacionViewModel.cargarNotificacionesDeUsuario(idActual)
        }
    }

    // Efecto para abrir el modal automáticamente si el usuario llega desde el toque de la notificación
    LaunchedEffect(notificaciones, notificacionIdInicial) {
        if (notificacionIdInicial != null && notificaciones.isNotEmpty() && notificacionSeleccionada == null && !deepLinkConsumido) {
            val notiEncontrada = notificaciones.find { it.id == notificacionIdInicial }
            if (notiEncontrada != null) {
                notificacionSeleccionada = notiEncontrada
                deepLinkConsumido = true
            }
        }
    }

    // Evaluamos si tiene el permiso dado cada vez que se carga esta pantalla
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            tienePermisoNotis = checkSelfPermission(contexto, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
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

            if (!tienePermisoNotis) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Si lo toca, lo lleva directo a la pantalla para darle permiso
                            val intent = Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, contexto.packageName)
                            }
                            contexto.startActivity(intent)
                        },
                    color = Color(0xFFFFF6ED)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.NotificationsOff, contentDescription = null, tint = Color(0xFFE85A13), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Notificaciones desactivadas. Toca aquí para habilitarlas en Ajustes y no perderte nada.",
                            color = Color(0xFFE85A13),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Filtros rapidos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val opcionesFiltro = listOf("Todas", "Solicitudes", "Favoritos", "Nuevas mascotas")
                opcionesFiltro.forEach { filtro ->
                    ChipFiltro(
                        texto = filtro,
                        seleccionado = notificacionViewModel.filtroActual == filtro,
                        alClickear = { notificacionViewModel.cambiarFiltro(filtro) }
                    )
                }
            }

            if (notificaciones.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Todavía no tienes notificaciones.", color = Color.Gray, fontSize = 16.sp)
                }
            } else {

                // Lista de Notificaciones
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notificaciones) { noti ->
                        ItemNotificacion(
                            notificacion = noti,
                            tiempoTranscurrido = notificacionViewModel.obtenerTiempoTranscurrido(
                                noti.fechaHora
                            ),
                            alClickear = {
                                notificacionViewModel.marcarComoLeida(noti)
                                notificacionSeleccionada = noti
                            }
                        )
                    }
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
                nombreEmisor = emisor?.nombre ?: "Cargando...",
                fotoEmisor = emisor?.imagen,
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
fun ChipFiltro(texto: String, seleccionado: Boolean, alClickear: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (seleccionado) Color(0xFFE85A13) else Color.White,
        border = if (!seleccionado) BorderStroke(1.dp, Color(0xFFEEEEEE)) else null,
        modifier = Modifier.clickable { alClickear() }
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
fun ItemNotificacion(notificacion: Notificacion, tiempoTranscurrido: String, alClickear: () -> Unit) {
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
                color = if (notificacion.tipo == Enums.TipoNotificacion.FAVORITO) Color(0xFFFFEbee) else Color(0xFFFFF3E0),
                modifier = Modifier.size(48.dp)
            ) {
                when (notificacion.tipo) {
                    Enums.TipoNotificacion.FAVORITO -> {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red, modifier = Modifier.padding(12.dp))
                    }
                    else -> { // MASCOTA_NUEVA o CONTACTO
                        if (!notificacion.imagen.isNullOrEmpty()) {
                            val contexto = LocalContext.current
                            coil.compose.SubcomposeAsyncImage(
                                model = coil.request.ImageRequest.Builder(contexto)
                                    .data(notificacion.imagen)
                                    .addHeader("User-Agent", "Mozilla/5.0 (Android; Mobile)")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Foto mascota",
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                loading = {
                                    CircularProgressIndicator(
                                        modifier = Modifier.padding(12.dp),
                                        color = Color(0xFFE85A13),
                                        strokeWidth = 2.dp
                                    )
                                },
                                error = {
                                    Icon(Icons.Default.Pets, contentDescription = null, tint = Color.Red, modifier = Modifier.padding(12.dp))
                                }
                            )
                        } else { // Si no hay foto
                            Icon(Icons.Default.Pets, contentDescription = null, tint = Color(0xFFE85A13), modifier = Modifier.padding(12.dp))
                        }
                    }
                }
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
                    text = tiempoTranscurrido,
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
    nombreEmisor: String,
    fotoEmisor: String?,
    onDismiss: () -> Unit,
    onNavegarAMascota: (String) -> Unit,
    onNavegarAPerfil: (String) -> Unit
) {
    val contexto = LocalContext.current

    val textoFecha = try {
        val fechaOriginal = java.time.LocalDateTime.parse(notificacion.fechaHora, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val formateador = java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy 'A LAS' hh:mm a", java.util.Locale("es", "AR"))
        "RECIBIDO EL ${fechaOriginal.format(formateador).uppercase()}"
    } catch (e: Exception) {
        "FECHA DESCONOCIDA"
    }

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
                    if (!fotoEmisor.isNullOrEmpty()) {
                        val contexto = LocalContext.current
                        coil.compose.SubcomposeAsyncImage(
                            model = coil.request.ImageRequest.Builder(contexto)
                                .data(fotoEmisor)
                                .addHeader("User-Agent", "Mozilla/5.0 (Android; Mobile)")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Foto del usuario",
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = {
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = Modifier.padding(24.dp),
                                    color = Color(0xFFE85A13),
                                    strokeWidth = 2.dp
                                )
                            },
                            error = {
                                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(16.dp), tint = Color.Red)
                            }
                        )
                    } else {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(16.dp), tint = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre y Mensaje
                Text(text = nombreEmisor, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF6ED),  // Naranja tenue
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notificacion.descripcion,
                        modifier = Modifier.padding(16.dp),
                        color = Color.DarkGray
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

                // Footer de tiempo
                Text(text = textoFecha, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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