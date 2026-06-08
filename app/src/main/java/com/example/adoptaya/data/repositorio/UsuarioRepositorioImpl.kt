package com.example.adoptaya.data.repositorio

import com.example.adoptaya.data.local.dao.UsuarioDao
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.data.remoto.UsuarioServicio
import com.example.adoptaya.dominio.repositorio.UsuarioRepositorio

class UsuarioRepositorioImpl(
    private val usuarioDao: UsuarioDao
) : UsuarioRepositorio {

    override suspend fun obtenerUsuarioPorId(id: String): Usuario? {
        return usuarioDao.obtenerPorId(id)
    }

    override suspend fun guardarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }
}