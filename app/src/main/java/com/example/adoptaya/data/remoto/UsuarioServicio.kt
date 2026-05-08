package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Usuario

class UsuarioServicio {

    fun obtenerUsuarios(): List<Usuario> {
        return listOf(
            Usuario (
                id = "1",
                nombre = "Juan",
                email = "juan@gmail.com"
            ),
            Usuario(
                id = "2",
                nombre = "Maria",
                email = "maria@gmail.com"
            )
        )
    }

    fun obtenerUsuarioPorId(id: String): Usuario? {
        return obtenerUsuarios().find { it.id == id }
    }


}