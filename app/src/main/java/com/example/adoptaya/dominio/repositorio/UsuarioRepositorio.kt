package com.example.adoptaya.dominio.repositorio

import com.example.adoptaya.data.model.Usuario

interface UsuarioRepositorio {
    fun obtenerUsuarios(): List<Usuario>

    fun obtenerUsuarioPorId(id: String): Usuario?
}