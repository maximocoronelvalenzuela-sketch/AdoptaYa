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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.util.hayConexionAInternet

@Composable
fun PantallaLogin(
    authViewModel: AuthViewModel,
    alNavegarRegistro: () -> Unit,
    alNavegarRecuperarPassword: () -> Unit,
    alIngresarComoInvitado: () -> Unit,
    alLoginExitoso: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val contexto = LocalContext.current
    var sinInternet by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Acepte o rechace, el usuario entra al Home
        alLoginExitoso()
    }

    LaunchedEffect(Unit) {
        sinInternet = !hayConexionAInternet(contexto)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
            //.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido!", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            Text(
                "Iniciá sesión para continuar",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico de Google") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagen =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = imagen,
                            contentDescription = "Alternar vista de contraseña"
                        )
                    }
                }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    enabled = !authViewModel.estaCargando,
                    onClick = {
                        authViewModel.limpiarTodosLosErrores()
                        alNavegarRecuperarPassword()
                    }
                ) {
                    Text("¿Olvidaste tu contraseña?", color = Color(0xFFE85A13))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authViewModel.mensajeError != null) {
                Text(
                    text = authViewModel.mensajeError!!,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    authViewModel.iniciarSesion(email, password, !sinInternet) {
                        // Si el login fue exitoso, comprobamos la versión de Android
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // Si es Android 13 o superior, pedimos el permiso.
                            // Al responder, el permissionLauncher ejecutará alLoginExitoso()
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            // En Android 12 o inferior, el permiso se otorga al instalar. Pasa directo al Home
                            alLoginExitoso()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !authViewModel.estaCargando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE85A13),
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (authViewModel.estaCargando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = alIngresarComoInvitado,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Continuar como invitado", color = Color.DarkGray, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿No tienes cuenta?", color = Color.Gray)
                TextButton(
                    enabled = !authViewModel.estaCargando,
                    onClick = {
                        authViewModel.limpiarTodosLosErrores()
                        alNavegarRegistro()
                    }
                ) {
                    Text("Registrate", color = Color(0xFFE85A13), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}