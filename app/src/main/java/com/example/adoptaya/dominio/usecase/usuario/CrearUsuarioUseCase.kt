package com.example.adoptaya.dominio.usecase.usuario

import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.dominio.repositorio.UsuarioRepositorio

class CrearUsuarioUseCase (
    private val usuarioRepositorio: UsuarioRepositorio
) {
    suspend operator fun invoke(usuario: Usuario) {
        usuarioRepositorio.guardarUsuario(usuario)
    }
}