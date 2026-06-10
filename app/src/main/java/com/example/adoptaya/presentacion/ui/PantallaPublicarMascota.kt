package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adoptaya.presentacion.viewmodel.PublicarMascotaViewModel

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
                    ) { Text("+", fontSize = 24.sp, color = Color.Gray) }
                    Box(
                        modifier = Modifier.fillMaxWidth().height(71.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEFE4D9)),
                        contentAlignment = Alignment.Center
                    ) { Text("+", fontSize = 24.sp, color = Color.Gray) }
                }
            }
            Text("Agregá hasta 3 fotos claras de tu mascota.", fontSize = 12.sp, color = Color.Gray)


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
                        onValueChange = { publicarMascotaViewModel.nombre = it },
                        label = { Text("Nombre de la mascota") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = colorFondoPantalla, focusedContainerColor = colorFondoPantalla)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // TODO: Lista Tipo
                        OutlinedTextField(
                            value = publicarMascotaViewModel.tipo,
                            onValueChange = {}, readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = publicarMascotaViewModel.raza,
                            onValueChange = { publicarMascotaViewModel.raza = it },
                            label = { Text("Raza (opcional)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = publicarMascotaViewModel.edadNumero,
                            onValueChange = { publicarMascotaViewModel.edadNumero = it },
                            label = { Text("Edad") },
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            // TODO: Lista Tiempos
                            value = publicarMascotaViewModel.edadTiempo,
                            onValueChange = {}, readOnly = true,
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                            modifier = Modifier.weight(0.8f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            // TODO: Lista Tamaño
                            value = publicarMascotaViewModel.tamano,
                            onValueChange = {}, readOnly = true,
                            label = { Text("Tamaño") },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
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
                        Checkbox(checked = publicarMascotaViewModel.vacunado, onCheckedChange = { publicarMascotaViewModel.vacunado = it }, colors = CheckboxDefaults.colors(checkedColor = colorNaranja))
                        Text("Vacunado")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = publicarMascotaViewModel.esterilizado, onCheckedChange = { publicarMascotaViewModel.esterilizado = it }, colors = CheckboxDefaults.colors(checkedColor = colorNaranja))
                        Text("Esterilizado")
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
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        publicarMascotaViewModel.rasgosDisponibles.forEach { rasgo ->
                            val seleccionado = publicarMascotaViewModel.rasgosSeleccionados.contains(rasgo)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, if (seleccionado) colorNaranja else Color.LightGray, RoundedCornerShape(16.dp))
                                    .background(if (seleccionado) colorNaranja.copy(alpha = 0.1f) else Color.Transparent)
                                    .clickable { publicarMascotaViewModel.toggleRasgo(rasgo) }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(text = rasgo, color = if (seleccionado) colorNaranja else Color.DarkGray)
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
                    OutlinedTextField(
                        value = publicarMascotaViewModel.barrio,
                        onValueChange = { publicarMascotaViewModel.barrio = it },
                        label = { Text("Barrio o Código Postal") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, tint = colorNaranja, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    // Placeholder del Mapa
                    Box(
                        modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(12.dp)).background(colorNaranja.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("TOCAR PARA AJUSTAR MAPA", color = colorNaranja, fontWeight = FontWeight.Bold)
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
                        onValueChange = { publicarMascotaViewModel.descripcionAdicional = it },
                        placeholder = { Text("Contanos su historia, personalidad y por qué necesita un nuevo hogar...") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = colorFondoPantalla, focusedContainerColor = colorFondoPantalla)
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