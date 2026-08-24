package com.example.adoptaya.dominio.usecase.auth

import com.example.adoptaya.dominio.repositorio.AuthRepositorio

class ObtenerIdUsuarioActualUseCase(
    private val authRepositorio: AuthRepositorio
) {
    operator fun invoke(): String? {
        return authRepositorio.obtenerIdUsuarioActual()
    }
}