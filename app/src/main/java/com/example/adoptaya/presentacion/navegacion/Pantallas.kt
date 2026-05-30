package com.example.adoptaya.presentacion.navegacion

sealed class Pantallas(val ruta: String) {

    object Mascotas : Pantallas("mascotas")

    object DetalleMascota : Pantallas("detalle_mascota/{mascotaId}") {

        fun crearRuta(mascotaId: String): String {
            return "detalle_mascota/$mascotaId"
        }
    }

    object Favoritos : Pantallas("favoritos")

    object Perfil : Pantallas("perfil")

    object Mapa : Pantallas("mapa")

    object Publicar : Pantallas("publicar")
}