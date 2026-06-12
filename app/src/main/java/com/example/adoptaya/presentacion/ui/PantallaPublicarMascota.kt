package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.presentacion.viewmodel.PublicarMascotaViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PantallaPublicarMascota(
    publicarMascotaViewModel: PublicarMascotaViewModel,
    onVolver: () -> Unit
) {
    val colorNaranja = Color(0xFFF28B2A)
    val colorFondoPantalla = Color(0xFFF3F3F3)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Publicación", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                actions = {
                    TextButton(onClick = { publicarMascotaViewModel.guardarMascota { onVolver() } }) {
                        Text("Publicar", color = colorNaranja, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = colorFondoPantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TODO: Fotos (Placeholder)
            Text("Fotos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEFE4D9)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Color.Gray)
                        Text("FOTO PRINCIPAL", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(71.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEFE4D9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 24.sp, color = Color.Gray)
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth().height(71.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEFE4D9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 24.sp, color = Color.Gray)
                    }
                }
            }
            Text("Agregá hasta 3 fotos claras de tu mascota.", fontSize = 12.sp, color = Color.DarkGray)


            // Informacion basica
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Información Básica", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    OutlinedTextField(
                        value = publicarMascotaViewModel.nombre,
                        onValueChange = {
                            publicarMascotaViewModel.nombre = it
                            publicarMascotaViewModel.nombreError = null
                        },
                        label = { Text("Nombre de la mascota") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = publicarMascotaViewModel.nombreError != null,
                        supportingText = {
                            if(publicarMascotaViewModel.nombreError != null) {
                                publicarMascotaViewModel.nombreError?.let { Text(it) }
                            }
                        }
                    )

                    // Categoria y Raza
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        var expandidoTipo by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandidoTipo,
                            onExpandedChange = { expandidoTipo = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = publicarMascotaViewModel.tipo.name.lowercase().replaceFirstChar { it.uppercase() },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Categoría") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoTipo) },
                                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expandidoTipo,
                                onDismissRequest = { expandidoTipo = false }
                            ) {
                                Enums.TipoMascota.entries.forEach { valorEnum ->
                                    val opcionTexto = valorEnum.name.lowercase().replaceFirstChar { it.uppercase() }
                                    DropdownMenuItem(
                                        text = { Text(opcionTexto) },
                                        onClick = {
                                            publicarMascotaViewModel.tipo = valorEnum
                                            expandidoTipo = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = publicarMascotaViewModel.raza,
                            onValueChange = { publicarMascotaViewModel.raza = it },
                            label = { Text("Raza (opcional)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Edad y Años/Meses
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = publicarMascotaViewModel.edad,
                            onValueChange = {
                                val inputFiltrado = it.filter{ char -> char.isDigit() }
                                publicarMascotaViewModel.edad = it
                                publicarMascotaViewModel.edadError = null
                            },
                            label = { Text("Edad") },
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            isError = publicarMascotaViewModel.edadError != null,
                            supportingText = {
                                if(publicarMascotaViewModel.edadError != null)
                                    publicarMascotaViewModel.edadError?.let { Text(it) }
                            }
                        )

                        var expandidoTiempo by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandidoTiempo,
                            onExpandedChange = { expandidoTiempo = it },
                            modifier = Modifier.weight(0.8f)
                        ) {
                            OutlinedTextField(
                                value = publicarMascotaViewModel.tiempo.name.lowercase().replaceFirstChar { it.uppercase() },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tiempo") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoTiempo) },
                                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expandidoTiempo,
                                onDismissRequest = { expandidoTiempo = false }) {
                                Enums.TiempoEdad.entries.forEach { valorEnum ->
                                    val opcionTexto = valorEnum.name.lowercase().replaceFirstChar { it.uppercase() }
                                    DropdownMenuItem(
                                        text = { Text(opcionTexto) },
                                        onClick = {
                                            publicarMascotaViewModel.tiempo = valorEnum
                                            expandidoTiempo = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Sexo y Tamaño
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        var expandidoSexo by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandidoSexo,
                            onExpandedChange = { expandidoSexo = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = publicarMascotaViewModel.sexo.name.lowercase().replaceFirstChar { it.uppercase() },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Sexo") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoSexo) },
                                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            ExposedDropdownMenu(expanded = expandidoSexo, onDismissRequest = { expandidoSexo = false }) {
                                Enums.SexoMascota.entries.forEach { valorEnum ->
                                    val opcionTexto = valorEnum.name.lowercase().replaceFirstChar { it.uppercase() }
                                    DropdownMenuItem(
                                        text = { Text(opcionTexto) },
                                        onClick = {
                                            publicarMascotaViewModel.sexo = valorEnum
                                            expandidoSexo = false
                                        }
                                    )
                                }
                            }
                        }

                        var expandidoTamano by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandidoTamano,
                            onExpandedChange = { expandidoTamano = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = publicarMascotaViewModel.tamaño.name.lowercase().replaceFirstChar { it.uppercase() },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tamaño") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoTamano) },
                                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            ExposedDropdownMenu(expanded = expandidoTamano, onDismissRequest = { expandidoTamano = false }) {
                                Enums.TamañoMascota.entries.forEach { valorEnum ->
                                    val opcionTexto = valorEnum.name.lowercase().replaceFirstChar { it.uppercase() }
                                    DropdownMenuItem(
                                        text = { Text(opcionTexto) },
                                        onClick = {
                                            publicarMascotaViewModel.tamaño = valorEnum
                                            expandidoTamano = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }


            // Estado de salud
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estado de Salud", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = publicarMascotaViewModel.vacunado,
                            onCheckedChange = { publicarMascotaViewModel.vacunado = it },
                            colors = CheckboxDefaults.colors(checkedColor = colorNaranja)
                        )
                        Text("Vacunado")
                        Spacer(modifier = Modifier.width(16.dp))

                        Checkbox(
                            checked = publicarMascotaViewModel.esterilizado,
                            onCheckedChange = { publicarMascotaViewModel.esterilizado = it },
                            colors = CheckboxDefaults.colors(checkedColor = colorNaranja)
                        )
                        Text("Esterilizado")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = publicarMascotaViewModel.desparasitado,
                            onCheckedChange = { publicarMascotaViewModel.desparasitado = it },
                            colors = CheckboxDefaults.colors(checkedColor = colorNaranja)
                        )
                        Text("Desparasitado")
                    }
                }
            }


            // Rasgos
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Rasgos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Input de texto y Boton Agregar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = publicarMascotaViewModel.rasgoInput,
                            onValueChange = { publicarMascotaViewModel.rasgoInput = it },
                            placeholder = { Text("Ej: Cariñoso") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { publicarMascotaViewModel.agregarRasgo() },
                            colors = ButtonDefaults.buttonColors(containerColor = colorNaranja),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text("Agregar")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // FlowRow para mostrar los chips como etiquetas
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        publicarMascotaViewModel.rasgosSeleccionados.forEach { rasgo ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(colorFondoPantalla)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                    .clickable { publicarMascotaViewModel.eliminarRasgo(rasgo) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = rasgo, color = Color.DarkGray, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Borrar",
                                        modifier = Modifier.size(14.dp),
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }


            // Ubicacion
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Ubicación", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    // Provincia
                    var expandidoProvincia by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandidoProvincia,
                        onExpandedChange = { expandidoProvincia = it }
                    ) {
                        OutlinedTextField(
                            value = publicarMascotaViewModel.provinciaSeleccionada?.nombre ?: "Seleccionar Provincia",
                            onValueChange = {}, readOnly = true,
                            label = { Text("Provincia") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoProvincia) },
                            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(expanded = expandidoProvincia, onDismissRequest = { expandidoProvincia = false }) {
                            publicarMascotaViewModel.listaProvincias.forEach { provincia ->
                                DropdownMenuItem(
                                    text = { Text(provincia.nombre) },
                                    onClick = {
                                        publicarMascotaViewModel.provinciaSeleccionada = provincia
                                        publicarMascotaViewModel.cargarLocalidadesPorProvincia(provincia) // Carga las ciudades!
                                        expandidoProvincia = false
                                    }
                                )
                            }
                        }
                    }

                    // Ciudad
                    var expandidoCiudad by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandidoCiudad,
                        onExpandedChange = { expandidoCiudad = it }
                    ) {
                        OutlinedTextField(
                            value = publicarMascotaViewModel.localidadSeleccionada?.nombre ?: "Seleccionar Ciudad",
                            onValueChange = {}, readOnly = true,
                            label = { Text("Ciudad / Municipio") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCiudad) },
                            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            enabled = publicarMascotaViewModel.provinciaSeleccionada != null // Bloqueado si no hay provincia
                        )
                        ExposedDropdownMenu(expanded = expandidoCiudad, onDismissRequest = { expandidoCiudad = false }) {
                            publicarMascotaViewModel.listaLocalidades.forEach { localidad ->
                                DropdownMenuItem(
                                    text = { Text(localidad.nombre) },
                                    onClick = {
                                        publicarMascotaViewModel.localidadSeleccionada = localidad
                                        expandidoCiudad = false
                                    }
                                )
                            }
                        }
                    }

                    // Barrio, Calle y Numero
                    OutlinedTextField(
                        value = publicarMascotaViewModel.barrio,
                        onValueChange = {
                            publicarMascotaViewModel.barrio = it
                            publicarMascotaViewModel.barrioError = null
                        },
                        label = { Text("Barrio") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = publicarMascotaViewModel.barrioError != null,
                        supportingText = {
                            if(publicarMascotaViewModel.barrioError != null)
                                publicarMascotaViewModel.barrioError?.let { Text(it) }
                        }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = publicarMascotaViewModel.calle,
                            onValueChange = {
                                publicarMascotaViewModel.calle = it
                                publicarMascotaViewModel.calleError = null
                            },
                            label = { Text("Calle") },
                            modifier = Modifier.weight(2f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            isError = publicarMascotaViewModel.calleError != null,
                            supportingText = {
                                if(publicarMascotaViewModel.calleError != null)
                                    publicarMascotaViewModel.calleError?.let { Text(it) }
                            }
                        )
                        OutlinedTextField(
                            value = publicarMascotaViewModel.numero,
                            onValueChange = {
                                publicarMascotaViewModel.numero = it.filter { char -> char.isDigit() }
                                publicarMascotaViewModel.numeroError = null
                            },
                            label = { Text("Número") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = publicarMascotaViewModel.numeroError != null,
                            supportingText = {
                                if(publicarMascotaViewModel.numeroError != null)
                                    publicarMascotaViewModel.numeroError?.let { Text(it) }
                            }
                        )
                    }

                    // Mapa
                    val contexto = LocalContext.current

                    // Boton para generar el mapa
                    Button(
                        onClick = { publicarMascotaViewModel.generarCoordenadasDesdeDireccion(contexto) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorNaranja),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("GENERAR UBICACIÓN EN EL MAPA", fontWeight = FontWeight.Bold)
                    }

                    // Mensaje de error de ubicacion (si falla el Geocoder o se olvida de mapear)
                    if (publicarMascotaViewModel.ubicacionError != null) {
                        Text(
                            text = publicarMascotaViewModel.ubicacionError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Mapa (Solo se muestra si la latitud y longitud existen)
                    if (publicarMascotaViewModel.latitud != null && publicarMascotaViewModel.longitud != null) {
                        val posicionMascota = LatLng(
                            publicarMascotaViewModel.latitud!!,
                            publicarMascotaViewModel.longitud!!
                        )

                        // Controla la camara del mapa
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(posicionMascota, 15f)
                        }

                        // Si la posicion cambia, movemos la camara automáticamente
                        LaunchedEffect(posicionMascota) {
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(posicionMascota, 15f)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, colorNaranja, RoundedCornerShape(16.dp))
                        ) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                //uiSettings = MapUiSettings(zoomControlsEnabled = false) // Mapa mas limpio
                            ) {
                                Marker(
                                    state = MarkerState(position = posicionMascota),
                                    title = "Ubicación de la mascota"
                                )
                            }
                        }
                    }
                }
            }


            // Acerca de la mascota
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Acerca de la Mascota", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = publicarMascotaViewModel.descripcionAdicional,
                        onValueChange = {
                            publicarMascotaViewModel.descripcionAdicional = it
                            publicarMascotaViewModel.descripcionError = null
                        },
                        placeholder = { Text("Contanos su historia, personalidad y por qué necesita un nuevo hogar...") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        isError = publicarMascotaViewModel.descripcionError != null,
                        supportingText = {
                            if(publicarMascotaViewModel.descripcionError != null)
                                publicarMascotaViewModel.descripcionError?.let { Text(it) }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))


            // Boton de Publicar
            Button(
                onClick = { publicarMascotaViewModel.guardarMascota { onVolver() } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorNaranja),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Crear Perfil", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}