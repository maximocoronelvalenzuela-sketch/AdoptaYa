package com.example.adoptaya.presentacion.componentes

import android.R.attr.text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun ModalRequiereLogin(
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Sesión requerida", fontWeight = FontWeight.Bold)
        },
        text = {
            Text("Para continuar con esta acción necesitas iniciar sesión o crear una cuenta en AdoptaYa.")
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE85A13))
            ) {
                Text("Iniciar Sesión")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text("Cancelar", color = Color.Black)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}