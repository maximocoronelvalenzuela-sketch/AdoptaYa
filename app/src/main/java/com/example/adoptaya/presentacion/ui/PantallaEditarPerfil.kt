package com.example.adoptaya.presentacion.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.adoptaya.presentacion.componentes.TopBar
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPerfil(
    usuarioViewModel: UsuarioViewModel,
    alVolver: () -> Unit
) {
    val contexto = LocalContext.current
    val usuario = usuarioViewModel.usuarioSeleccionado

    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var provincia by remember { mutableStateOf(usuario?.provincia ?: "") }
    var ciudad by remember { mutableStateOf(usuario?.ciudad ?: "") }
    var imagenSeleccionada by remember { mutableStateOf(usuario?.imagen) }

    val hayCambiosSinGuardar = nombre != (usuario?.nombre ?: "") ||
            provincia != (usuario?.provincia ?: "") ||
            ciudad != (usuario?.ciudad ?: "") ||
            imagenSeleccionada != usuario?.imagen

    var mostrarDialogoDescarte by remember { mutableStateOf(false) }

    // Launcher de la galería (para 1 sola imagen)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imagenSeleccionada = usuarioViewModel.guardarImagenEnLocal(contexto, uri)
        }
    }

    // Activa el modal de confirmacion cuando se vuelve con el boton de Volver atras del propio celular
    BackHandler(enabled = hayCambiosSinGuardar) {
        mostrarDialogoDescarte = true
    }

    if (mostrarDialogoDescarte) {
        DialogoDescarteCambios(
            onConfirmarSalir = {
                mostrarDialogoDescarte = false
                alVolver()
            },
            onCancelar = { mostrarDialogoDescarte = false }
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                titulo = "Editar Perfil",
                mostrarBotonVolver = true,
                alVolver = {
                    if (hayCambiosSinGuardar) mostrarDialogoDescarte = true else alVolver()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de perfil
            FotoPerfilEditable(
                imagenSeleccionada = imagenSeleccionada,
                alClickearCambiar = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Provincia
            SelectorDesplegable(
                label = "Provincia",
                valorActual = provincia.ifEmpty { "Seleccionar Provincia" },
                opciones = usuarioViewModel.listaProvincias.map { it.nombre },
                onOpcionSeleccionada = { nombreProvincia ->
                    provincia = nombreProvincia
                    ciudad = ""
                    // Buscamos el objeto (Provincia) original de la API para buscar sus localidades
                    val provAPI = usuarioViewModel.listaProvincias.find { it.nombre == nombreProvincia }
                    if (provAPI != null) usuarioViewModel.cargarLocalidadesPorProvincia(provAPI)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ciudad
            SelectorDesplegable(
                label = "Ciudad / Municipio",
                valorActual = ciudad.ifEmpty { "Seleccionar Ciudad" },
                opciones = usuarioViewModel.listaLocalidades.map { it.nombre },
                habilitado = provincia.isNotEmpty(),
                onOpcionSeleccionada = { ciudad = it }
            )

            if (usuarioViewModel.errorApiGeoref != null) {
                Text(
                    text = usuarioViewModel.errorApiGeoref!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    usuarioViewModel.actualizarDatosPerfil(
                        contexto = contexto,
                        nombreNuevo = nombre,
                        provinciaNueva = provincia,
                        ciudadNueva = ciudad,
                        imagenNueva = imagenSeleccionada
                    ) {
                        Toast.makeText(contexto, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                        alVolver()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !usuarioViewModel.estaGuardandoPerfil && hayCambiosSinGuardar && nombre.isNotBlank() && ciudad.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE85A13),
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (usuarioViewModel.estaGuardandoPerfil) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DialogoDescarteCambios(
    onConfirmarSalir: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Descartar cambios", fontWeight = FontWeight.Bold) },
        text = { Text("Tienes cambios sin guardar. ¿Seguro que quieres salir?") },
        confirmButton = {
            TextButton(onClick = onConfirmarSalir) { Text("Salir sin guardar", color = Color.Red) }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}

@Composable
fun FotoPerfilEditable(
    imagenSeleccionada: String?,
    alClickearCambiar: () -> Unit
) {
    // "BottomEnd" tira los componetes abajo a la derecha
    Box(contentAlignment = Alignment.BottomEnd) {
        Surface(
            shape = CircleShape,
            color = Color.LightGray,
            modifier = Modifier.size(100.dp).clickable { alClickearCambiar() }
        ) {
            if (imagenSeleccionada != null) {
                AsyncImage(
                    model = imagenSeleccionada,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(24.dp), tint = Color.DarkGray)
            }
        }
        Surface(
            shape = CircleShape,
            color = Color(0xFFE85A13),
            modifier = Modifier.size(32.dp).clickable { alClickearCambiar() }
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Cambiar foto", tint = Color.White, modifier = Modifier.padding(6.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorDesplegable(
    label: String,
    valorActual: String,
    opciones: List<String>,
    habilitado: Boolean = true,
    onOpcionSeleccionada: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { if (habilitado) expandido = it }
    ) {
        OutlinedTextField(
            value = valorActual,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = habilitado
        )
        ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onOpcionSeleccionada(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}