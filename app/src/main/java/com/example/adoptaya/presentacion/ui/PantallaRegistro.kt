package com.example.adoptaya.presentacion.ui

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel

@Composable
fun PantallaRegistro(
    authViewModel: AuthViewModel,
    alNavegarLogin: () -> Unit,
    alRegistroExitoso: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        alRegistroExitoso()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
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
                authViewModel.limpiarError("nombre")
            },
            label = { Text("Nombre completo") },
            isError = authViewModel.mensajeErrorNombre != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        if (authViewModel.mensajeErrorNombre != null) {
            Text(text = authViewModel.mensajeErrorNombre!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                authViewModel.limpiarError("email")
            },
            label = { Text("Correo electrónico de Google") },
            placeholder = { Text("Ej: ejemplo@gmail.com") },
            isError = authViewModel.mensajeErrorEmail != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        if (authViewModel.mensajeErrorEmail != null) {
            Text(text = authViewModel.mensajeErrorEmail!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { input ->
                val soloNumeros = input.filter { it.isDigit() }
                if (soloNumeros.length <= 10) {
                    telefono = soloNumeros
                    authViewModel.limpiarError("telefono")
                }
            },
            label = { Text("Celular (sin 0 ni 15)") },
            placeholder = { Text("Ej: 3851234567") },
            leadingIcon = {
                Text(
                    text = "+54 9",
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                )
            },
            isError = authViewModel.mensajeErrorTelefono != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
            )
        )
        if (authViewModel.mensajeErrorTelefono != null) {
            Text(text = authViewModel.mensajeErrorTelefono!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                authViewModel.limpiarError("password")
            },
            label = { Text("Contraseña (min. 6 caracteres)") },
            isError = authViewModel.mensajeErrorPassword != null,
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
        if (authViewModel.mensajeErrorPassword != null) {
            Text(text = authViewModel.mensajeErrorPassword!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                authViewModel.registrar(nombre, email, password, telefono) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        alRegistroExitoso()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !authViewModel.estaCargando,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE85A13)),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (authViewModel.estaCargando) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        if (authViewModel.mensajeError != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = authViewModel.mensajeError!!, color = Color.Red, fontWeight = FontWeight.Bold)
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