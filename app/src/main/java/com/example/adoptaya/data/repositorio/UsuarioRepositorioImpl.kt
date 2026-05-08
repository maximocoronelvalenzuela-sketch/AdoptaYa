package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.data.remoto.UsuarioServicio
import com.example.adoptaya.dominio.repositorio.UsuarioRepositorio

class UsuarioRepositorioImpl (
    private val servicio: UsuarioServicio
) : UsuarioRepositorio {

    override fun obtenerUsuarios(): List<Usuario> {
        return servicio.obtenerUsuarios()
    }

    override fun obtenerUsuarioPorId(id: String): Usuario? {
        return servicio.obtenerUsuarioPorId(id)
    }
}