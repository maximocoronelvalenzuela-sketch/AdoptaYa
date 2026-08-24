package com.example.adoptaya.dominio.usecase.auth

import com.example.adoptaya.dominio.repositorio.AuthRepositorio

class CerrarSesionUseCase(
    private val authRepositorio: AuthRepositorio
) {
    operator fun invoke() {
        authRepositorio.cerrarSesion()
    }
}