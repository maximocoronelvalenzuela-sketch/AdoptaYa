package com.example.adoptaya.presentacion.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.util.hayConexionAInternet

@Composable
fun PantallaRecuperarPassword(
    authViewModel: AuthViewModel,
    alVolver: () -> Unit
) {
    val contexto = LocalContext.current
    var email by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var sinInternet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        sinInternet = !hayConexionAInternet(contexto)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            //.padding(24.dp)
    ) {
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

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ) {
            IconButton(
                onClick = alVolver,
                modifier = Modifier.padding(bottom = 16.dp),
                enabled = !authViewModel.estaCargando
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }

            Text("Recuperar Contraseña", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text(
                "Ingresá tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (errorEmail != null) errorEmail = null
                },
                label = { Text("Correo electrónico de Google") },
                isError = errorEmail != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !authViewModel.estaCargando
            )
            if (errorEmail != null) {
                Text(
                    text = errorEmail!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (!email.lowercase().endsWith("@gmail.com")) {
                        errorEmail = "El Email debe ser de Google (@gmail.com)"
                    } else {
                        authViewModel.enviarCorreoRecuperacion(
                            email = email,
                            hayInternet = !sinInternet,
                            onExito = {
                                Toast.makeText(
                                    contexto,
                                    "Correo de recuperación enviado",
                                    Toast.LENGTH_LONG
                                ).show()
                                alVolver()
                            },
                            onError = { mensajeError ->
                                errorEmail = mensajeError
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !authViewModel.estaCargando && email.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE85A13),
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (authViewModel.estaCargando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Enviar enlace de recuperación",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}