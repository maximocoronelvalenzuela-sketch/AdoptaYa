package com.example.adoptaya.dominio.usecase.usuario

import com.example.adoptaya.data.model.Usuario

class VerificarPerfilIncompletoUseCase {
    operator fun invoke(usuario: Usuario?): Boolean {
        if (usuario == null) return false // Usuario invitado

        return usuario.provincia.isBlank() ||
                usuario.ciudad.isBlank() ||
                usuario.latitud == 0.0 ||
                usuario.longitud == 0.0 ||
                usuario.imagen == null
    }
}