package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaRegistro(
    alNavegarLogin: () -> Unit,
    alRegistrar: (String, String, String) -> Unit // Recibe nombre, email y contraseña
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Cuenta", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Text("Unite para adoptar o publicar mascotas", color = Color.Gray, modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                if (errorNombre != null) errorNombre = null
            },
            label = { Text("Nombre completo") },
            isError = errorNombre != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        if (errorNombre != null) {
            Text(text = errorNombre!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (errorPassword != null) errorPassword = null
            },
            label = { Text("Contraseña (min. 6 caracteres)") },
            isError = errorPassword != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val imagen = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = imagen, contentDescription = "Alternar vista de contraseña")
                }
            }
        )
        if (errorPassword != null) {
            Text(text = errorPassword!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                var todoValido = true

                // Validaciones
                if (nombre.isBlank()) {
                    errorNombre = "El nombre no puede estar vacío"
                    todoValido = false
                }
                if (!email.lowercase().endsWith("@gmail.com")) {
                    errorEmail = "El eMail debe ser de Google (@gmail.com)"
                    todoValido = false
                }
                if (password.length < 6) {
                    errorPassword = "La contraseña debe tener al menos 6 caracteres"
                    todoValido = false
                }

                if (todoValido) {
                    alRegistrar(nombre, email, password)
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE85A13)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿Ya tienes cuenta?", color = Color.Gray)
            TextButton(onClick = alNavegarLogin) {
                Text("Inicia sesión", color = Color(0xFFE85A13), fontWeight = FontWeight.Bold)
            }
        }
    }
}