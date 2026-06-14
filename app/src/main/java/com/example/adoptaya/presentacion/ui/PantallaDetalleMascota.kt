package com.example.adoptaya.presentacion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import kotlinx.coroutines.launch

@Composable
fun PantallaDetalleMascota(
    mascotaId: String,
    mascotaViewModel: MascotaViewModel,
    alVolver: () -> Unit
) {
    val mascota = mascotaViewModel.mascotas.find { it.id == mascotaId }
    val scrollState = rememberScrollState()

    if (mascota == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Mascota no encontrada")
        }
        return
    }

    Scaffold(
        bottomBar = { BarraContactoInferior() }    // Boton de WhatsApp fijo abajo
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Contenido scrolleable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Carrusel
                CarruselImagenesMascota(imagenes = mascota.imagenes)

                // Tarjeta blanca principal
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-32).dp), // Para superponerse a la foto
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color(0xFFF3F3F3)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                    ) {
                        // Nombre, Raza (Tipo, por ahora), Genero
                        val tipoFormateado = mascota.tipo.name.lowercase().replaceFirstChar { it.uppercase() }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = mascota.nombre,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 32.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = mascota.raza ?: tipoFormateado,
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }

                            // Tag de Genero (Naranja)
                            val sexoFormateado = mascota.sexo.name.lowercase().replaceFirstChar { it.uppercase() }

                            Surface(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val iconoSexo = if (mascota.sexo == Enums.SexoMascota.MACHO) Icons.Default.Male else Icons.Default.Female
                                    Icon(iconoSexo, contentDescription = null, tint = Color(0xFFE85A13), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(sexoFormateado, color = Color(0xFFE85A13), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Informacion basica
                        val tiempoFormateado = mascota.tiempo.name.lowercase().replaceFirstChar { it.uppercase() }
                        val tamanoFormateado = mascota.tamaño.name.lowercase().replaceFirstChar { it.uppercase() }

                        TituloSeccion("Información Básica")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CardInfoBasica(modifier = Modifier.weight(1f), icono = Icons.Default.CalendarToday, titulo = "EDAD", valor = mascota.edad + " " + tiempoFormateado)
                            CardInfoBasica(modifier = Modifier.weight(1f), icono = Icons.Default.Straighten, titulo = "TAMAÑO", valor = tamanoFormateado)
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Salud
                        TituloSeccion("Salud")
                        ItemSalud("Vacunado", estado = mascota.vacunado)
                        Spacer(modifier = Modifier.height(12.dp))
                        ItemSalud("Esterilizado/a / Castrado/a", estado = mascota.esterilizado)
                        Spacer(modifier = Modifier.height(12.dp))
                        ItemSalud("Desparasitado", estado = mascota.desparasitado)

                        Spacer(modifier = Modifier.height(32.dp))

                        // Personalidad
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TituloSeccion("Personalidad")
                            Text("RASGOS", color = Color(0xFFE85A13), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        mascota.descripcionPersonalidad.chunked(2).forEach { fila ->    // chunked(2) agrupa los elementos en una sublista de a 2
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                fila.forEach { rasgo ->
                                    ChipPersonalidad(rasgo)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Sobre la mascota
                        Surface(
                            color = Color(0xFFFCEADE), // Naranja muy tenue
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text("Sobre "+mascota.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = mascota.descripcionAdicional,
                                    color = Color.DarkGray,
                                    lineHeight = 24.sp,
                                    fontSize = 15.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Ubicacion aproximada
                        TituloSeccion("Ubicación aproximada")
                        Box(    // Falso mapa
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFA3C9A8)), // Color verde agua simulando el mapa
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp),
                                shadowElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Pets, contentDescription = null, tint = Color(0xFFE85A13))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(mascota.barrio, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                        Text(mascota.ciudad, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Botones: Atras y Favorito
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .statusBarsPadding(),   // Para que no esté bajo la barra de estado
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButtonFlotante(icono = Icons.Default.ArrowBackIosNew, onClick = alVolver)
                IconButtonFlotante(icono = Icons.Default.FavoriteBorder, onClick = { /* TODO: Agregar a favoritos */ })
            }
        }
    }
}

// Componentes de la misma pantalla (modularidad)

@Composable
fun IconButtonFlotante(icono: ImageVector, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.3f), // Fondo semitransparente
        modifier = Modifier.size(44.dp),
        onClick = onClick
    ) {
        Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun CardInfoBasica(modifier: Modifier = Modifier, icono: ImageVector, titulo: String, valor: String) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE5E8E8) // Fondo gris claro
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = Color(0xFFE85A13), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(titulo, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(valor, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ItemSalud(texto: String, estado: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (estado) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                tint = if (estado) Color(0xFF4CAF50) else Color(0xFFF44336),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(texto, color = Color.DarkGray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ChipPersonalidad(texto: String) {
    Surface(
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFE85A13), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(texto, color = Color.DarkGray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun BarraContactoInferior() {
    Surface(
        color = Color.White,
        shadowElevation = 16.dp, // Sombra para separarlo del contenido
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Boton Naranja de WhatsApp
            Button (
                onClick = { /* TODO: Abrir WhatsApp */ },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE85A13)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contactar por WhatsApp", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CarruselImagenesMascota(imagenes: List<String>) {
    // Si por algun error la mascota no tiene imagenes, se muestra un fondo gris liso
    if (imagenes.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().height(350.dp).background(Color.LightGray))
        return
    }

    // El estado del carrusel (cuantas paginas hay y cual se muestra)
    val pagerState = rememberPagerState(pageCount = { imagenes.size })
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {

        // Carrusel
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pagina ->
            AsyncImage(
                model = imagenes[pagina],
                contentDescription = "Foto "+(pagina + 1)+" de la mascota",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Botones
        if (imagenes.size > 1) {

            // Boton Atras
            if (pagerState.currentPage > 0) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.4f), shape = CircleShape)
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Anterior", tint = Color.White)
                }
            }

            // Boton Adelante
            if (pagerState.currentPage < imagenes.size - 1) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.4f), shape = CircleShape)
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Siguiente", tint = Color.White)
                }
            }

            // Los puntos indicadores en la parte de abajo
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(imagenes.size) { iteracion ->
                    val colorPunto = if (pagerState.currentPage == iteracion) Color.White else Color.White.copy(alpha = 0.5f)
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(colorPunto)
                    )
                }
            }
        }
    }
}