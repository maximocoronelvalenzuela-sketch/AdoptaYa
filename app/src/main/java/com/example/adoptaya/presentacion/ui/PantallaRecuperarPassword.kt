package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaRecuperarPassword(
    alVolver: () -> Unit,
    alEnviarCorreo: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        IconButton(onClick = alVolver, modifier = Modifier.padding(bottom = 16.dp)) {
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
            shape = RoundedCornerShape(12.dp)
        )
        if (errorEmail != null) {
            Text(text = errorEmail!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (!email.lowercase().endsWith("@gmail.com")) {
                    errorEmail = "El Email debe ser de Google (@gmail.com)"
                } else {
                    alEnviarCorreo(email)
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE85A13)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Enviar enlace de recuperación", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}