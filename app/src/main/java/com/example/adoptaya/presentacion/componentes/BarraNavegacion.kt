package com.example.adoptaya.presentacion.componentes

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.adoptaya.presentacion.navegacion.Pantallas

@Composable
fun BarraNavegacion(
    rutaActual: String?,
    alNavegar: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 12.dp // Sombra
    ) {
        // Home
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (rutaActual == Pantallas.Mascotas.ruta) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Inicio",
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text("Inicio") },
            selected = rutaActual == Pantallas.Mascotas.ruta,
            onClick = { alNavegar(Pantallas.Mascotas.ruta) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFE85A13),  // Naranja de la app
                selectedTextColor = Color(0xFFE85A13),
                indicatorColor = Color(0xFFFFF3E0)  // Fondo naranja al seleccionar
            )
        )

        // Favoritos
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (rutaActual == Pantallas.Favoritos.ruta) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favoritos",
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text("Favoritos") },
            selected = rutaActual == Pantallas.Favoritos.ruta,
            onClick = { alNavegar(Pantallas.Favoritos.ruta) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFE85A13),
                selectedTextColor = Color(0xFFE85A13),
                indicatorColor = Color(0xFFFFF3E0)
            )
        )

        // Publicar (Boton Central)
        NavigationBarItem(
            selected = rutaActual == Pantallas.Publicar.ruta,
            onClick = { alNavegar(Pantallas.Publicar.ruta) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Publicar",
                    modifier = Modifier.size(32.dp),    // Mas grande para destacar
                    tint = Color(0xFFE85A13)
                )
            },
            label = { Text("Publicar") }
        )

        // Mapa
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (rutaActual == Pantallas.Mapa.ruta) Icons.Filled.Map else Icons.Outlined.Map,
                    contentDescription = "Mapa",
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text("Mapa") },
            selected = rutaActual == Pantallas.Mapa.ruta,
            onClick = { alNavegar(Pantallas.Mapa.ruta) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFE85A13),
                selectedTextColor = Color(0xFFE85A13),
                indicatorColor = Color(0xFFFFF3E0)
            )
        )

        // Perfil
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (rutaActual == Pantallas.Perfil.ruta) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text("Perfil") },
            selected = rutaActual == Pantallas.Perfil.ruta,
            onClick = { alNavegar(Pantallas.Perfil.ruta) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFE85A13),
                selectedTextColor = Color(0xFFE85A13),
                indicatorColor = Color(0xFFFFF3E0)
            )
        )
    }
}