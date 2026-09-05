package com.example.adoptaya.presentacion.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota

@Composable
fun MascotaCard(
    mascota: Mascota,
    nombreDueño: String,
    fotoDueño: String?,
    tiempoTranscurrido: String,
    alClickearMascota: () -> Unit,
    alClickearPerfilDueño: () -> Unit,
    alClickearFavorito: () -> Unit,
    alClickearBorrar: () -> Unit = {},
    alCambiarEstado: (Enums.EstadoMascota) -> Unit = {},
    esFavorito: Boolean = false,
    esMiMascota: Boolean = false,
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(24.dp),  // Esquinas redondeadas
        colors = CardDefaults.cardColors(
            containerColor = Color.White    // Fondo de la Card blanca
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp // Sombra
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { alClickearMascota() }
            ) {
                // Foto principal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp) // Altura fija para que todas las fotos se vean uniformes
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    // La imagen de internet
                    AsyncImage(
                        // Toma la primera imagen de la lista.
                        model = mascota.imagenes.firstOrNull() ?: "https://demofree.sirv.com/nope-not-here.jpg",
                        contentDescription = "Foto de " + mascota.nombre,
                        contentScale = ContentScale.Crop, // Recorta la foto para que llene el rectángulo perfecto
                        modifier = Modifier.fillMaxSize()
                    )

                    // Estado y Boton de Favoritos
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,   // Separa los elementos a los extremos
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var menuExpandido by remember { mutableStateOf(false) }
                        val esDisponible = mascota.estado == Enums.EstadoMascota.DISPONIBLE
                        val colorFondo = if (esDisponible) Color(0xFFE8F5E9) else Color(0xFFFFF3E0) // Verde / Naranja
                        val colorTexto = if (esDisponible) Color(0xFF2E7D32) else Color(0xFFE65100)
                        val colorPunto = if (esDisponible) Color(0xFF4CAF50) else Color(0xFFFF9800)

                        Box {
                            // Etiqueta de Estado (DISPONIBLE o ADOPTADO)
                            Surface(
                                color = colorFondo,
                                shape = RoundedCornerShape(50), // Ovalado
                                modifier = if (esMiMascota) {
                                    Modifier.clickable { menuExpandido = true }
                                } else Modifier
                            ) {
                                // Puntito y texto uno al costado del otro
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 6.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Puntito
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(colorPunto, shape = CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text(
                                        text = mascota.estado.name,
                                        color = colorTexto, // Verde oscuro
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )

                                    if (esMiMascota) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Cambiar estado",
                                            tint = colorTexto,
                                            modifier = Modifier.size(20.dp).padding(start = 4.dp)
                                        )
                                    }
                                }
                            }

                            DropdownMenu(
                                expanded = menuExpandido,
                                onDismissRequest = { menuExpandido = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text("DISPONIBLE", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                    },
                                    onClick = {
                                        alCambiarEstado(Enums.EstadoMascota.DISPONIBLE)
                                        menuExpandido = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text("ADOPTADO", fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                                    },
                                    onClick = {
                                        alCambiarEstado(Enums.EstadoMascota.ADOPTADO)
                                        menuExpandido = false
                                    }
                                )
                            }
                        }

                        // Botones Favorito + Borrar
                        Row {
                            // Borrar
                            if (esMiMascota) {
                                IconButton(
                                    onClick = { alClickearBorrar() },
                                    modifier = Modifier.size(32.dp),
                                    colors = IconButtonDefaults.iconButtonColors(Color.Black.copy(alpha = 0.3f))
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Borrar publicación", tint = Color.White)
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            // Favoritos (Corazón)
                            IconButton(
                                onClick = { alClickearFavorito() },
                                modifier = Modifier.size(32.dp),
                                colors = IconButtonDefaults.iconButtonColors(Color.Black.copy(alpha = 0.3f))
                            ) {
                                Icon(
                                    imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Agregar a favoritos",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Nombre de la mascota
                        Text(
                            text = mascota.nombre,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,  // Letra bien gruesa en negrita
                                color = Color(0xFF1A1A1A)
                            )
                        )

                        // Lógica para elegir el ícono según el sexo
                        val iconoSexo = if (mascota.sexo == Enums.SexoMascota.MACHO) {
                            Icons.Default.Male
                        } else {
                            Icons.Default.Female
                        }

                        // Contenedor cuadradito para el ícono de sexo (inspirado en tu diseño)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF3F3F3),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = iconoSexo,
                                contentDescription = "Sexo de la mascota",
                                modifier = Modifier.padding(6.dp),
                                tint = Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Formato "Raza • Edad • Ciudad, Barrio"
                    val textoDetalles = if (!mascota.raza.isNullOrBlank()) {
                        mascota.raza + " • " + mascota.edad + " años • " + mascota.ciudad+", "+mascota.barrio
                    } else {
                        mascota.edad + " años • " + mascota.ciudad+", "+mascota.barrio
                    }

                    Text(
                        text = textoDetalles,
                        color = Color(0xFF666666),  // Gris mas claro que el nombre
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2, // Si pasa las dos líneas, se corta
                        overflow = TextOverflow.Ellipsis // Esto agrega automáticamente el "..." al final
                    )
                }

                HorizontalDivider(  // Linea separadora
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            // Seccion del usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { alClickearPerfilDueño() }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Foto del Usuario
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = Color.LightGray
                ) {
                    if (!fotoDueño.isNullOrEmpty()) {
                        coil.compose.AsyncImage(
                            model = fotoDueño,
                            contentDescription = "Foto de perfil de $nombreDueño",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto por defecto",
                            modifier = Modifier.padding(8.dp),
                            tint = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Columna de Nombre + Tiempo de publicacion
                Column(modifier = Modifier.weight(1f)) {
                    // Nombre del Usuario
                    Text(
                        text = nombreDueño,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    // Tiempo transcurrido
                    Text(
                        text = "Subido $tiempoTranscurrido",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}