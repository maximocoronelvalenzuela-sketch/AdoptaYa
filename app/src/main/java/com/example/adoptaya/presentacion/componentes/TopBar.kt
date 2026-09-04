package com.example.adoptaya.presentacion.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adoptaya.util.hayConexionAInternet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    titulo: String,
    color: Color = Color.Black,
    tamañoFuente: TextUnit = 22.sp,
    mostrarBotonVolver: Boolean = false,
    alVolver: () -> Unit = {},
    mostrarBotonNotificaciones: Boolean = false,
    alClickearNotificaciones: () -> Unit = {},
    mostrarBotonLogin: Boolean = false,
    alClickearLogin: () -> Unit = {}
) {
    val contexto = LocalContext.current
    var sinInternet by remember { mutableStateOf(false) }

    // Evaluamos la conexión al dibujar la barra
    LaunchedEffect(Unit) {
        sinInternet = !hayConexionAInternet(contexto)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Text(
                    text = titulo,
                    color = color,
                    fontSize = tamañoFuente,
                    fontWeight = FontWeight.Bold
                )
            },
            // Boton de volver
            navigationIcon = {
                if (mostrarBotonVolver) {
                    IconButton(onClick = alVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            },
            // Botones de la derecha
            actions = {
                if (mostrarBotonLogin) {
                    OutlinedButton(
                        onClick = alClickearLogin,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFFE85A13)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFE85A13))
                    ) {
                        Text("Iniciar Sesión", fontWeight = FontWeight.Bold)
                    }
                }

                if (mostrarBotonNotificaciones) {
                    IconButton(onClick = alClickearNotificaciones) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = Color.Black
                        )
                    }
                }
            }
        )

        if (sinInternet) {
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.Red) {
                Text(
                    text = "Sin conexión a Internet.",
                    color = Color.White,
                    modifier = Modifier.padding(4.dp),
                    fontSize = 12.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}