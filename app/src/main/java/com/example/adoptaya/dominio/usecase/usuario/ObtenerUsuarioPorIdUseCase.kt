package com.example.adoptaya.dominio.usecase.usuario

import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.dominio.repositorio.UsuarioRepositorio

class ObtenerUsuarioPorIdUseCase (
    private val repositorio: UsuarioRepositorio
) {

    operator fun invoke(id: String): Usuario? {
        return repositorio.obtenerUsuarioPorId(id)
    }
}