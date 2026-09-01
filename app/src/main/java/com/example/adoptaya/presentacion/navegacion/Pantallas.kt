package com.example.adoptaya.presentacion.navegacion

sealed class Pantallas(val ruta: String) {

    object Mascotas : Pantallas("mascotas")

    object DetalleMascota : Pantallas("detalle_mascota/{mascotaId}") {

        fun crearRuta(mascotaId: String): String {
            return "detalle_mascota/$mascotaId"
        }
    }

    object Favoritos : Pantallas("favoritos")

    object Perfil : Pantallas("perfil/{usuarioId}") {

        fun crearRuta(usuarioId: String): String {
            return "perfil/$usuarioId"
        }
    }

    object Mapa : Pantallas("mapa")

    object Publicar : Pantallas("publicar")

    object Notificaciones : Pantallas("notificaciones")

    object Login : Pantallas("login")

    object Registro : Pantallas("registro")

    object RecuperarPassword : Pantallas("recuperar_password")

    object EditarPerfil : Pantallas("editar_perfil")
}