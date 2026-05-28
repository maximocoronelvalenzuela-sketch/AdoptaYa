package com.example.adoptaya.presentacion.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adoptaya.data.model.Mascota

@Composable
fun MascotaCard(
    mascota: Mascota,
    alClickear: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                alClickear()
            },
        shape = RoundedCornerShape(24.dp),  // Esquinas redondeadas
        colors = CardDefaults.cardColors(
            containerColor = Color.White    // Fondo de la Card blanca
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp // Sombra
        )
    ) {

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,   // Separa los elementos a los extremos
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Etiqueta de Estado ("DISPONIBLE")
                Surface(
                    color = Color(0xFFE8F5E9),  // Fondo verde muy clarito
                    shape = RoundedCornerShape(50), // Ovalado
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    // Puntito y texto uno al costado del otro
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Puntito
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    Color(0xFF4CAF50),
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "DISPONIBLE",
                            color = Color(0xFF2E7D32), // Verde oscuro
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Botón de Favoritos (Corazón)
                IconButton(
                    onClick = { /* TODO: Lógica para agregar a favoritos */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder, // Usar Icons.Default.Favorite para el corazón lleno
                        contentDescription = "Agregar a favoritos",
                        tint = Color.Gray
                    )
                }
            }


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
                val iconoSexo = if (mascota.sexo == "Macho" || mascota.sexo == "Masculino") {
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

            // Formato "Raza (tipo por ahora) • Edad • Barrio"
            Text(
                text = mascota.tipo+" • "+mascota.edad+" años • Cabildo",
                color = Color(0xFF666666),  // Gris mas claro que el nombre
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2, // Si pasa las dos líneas, se corta
                overflow = TextOverflow.Ellipsis // Esto agrega automáticamente el "..." al final
            )


            // Seccion de usuario
            HorizontalDivider(  // Linea separadora
                modifier = Modifier.padding(vertical = 12.dp)
                //color = Color(0xFFF3F3F3) // Un gris muy tenue
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Foto del Usuario
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = Color.LightGray
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, // Reemplazar en el futuro por su respectiva foto
                        contentDescription = "Foto de perfil del usuario",
                        modifier = Modifier.padding(8.dp),
                        tint = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Columna de Nombre + Tiempo de publicacion
                Column(modifier = Modifier.weight(1f)) {
                    // Nombre del Usuario
                    Text(
                        text = "Maximo Coronel",    // HARDCODEADO por ahora
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    // Tiempo transcurrido
                    Text(
                        text = "Subido hace 3 horas",   // HARDCODEADO por ahora
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}