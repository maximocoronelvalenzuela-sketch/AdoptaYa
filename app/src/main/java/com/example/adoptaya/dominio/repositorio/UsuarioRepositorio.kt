package com.example.adoptaya.dominio.repositorio

import com.example.adoptaya.data.model.Usuario

interface UsuarioRepositorio {
    suspend fun obtenerUsuarioPorId(id: String): Usuario?

    suspend fun guardarUsuario(usuario: Usuario)
}