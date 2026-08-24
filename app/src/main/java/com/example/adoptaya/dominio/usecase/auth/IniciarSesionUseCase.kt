package com.example.adoptaya.dominio.usecase.auth

import com.example.adoptaya.dominio.repositorio.AuthRepositorio

class IniciarSesionUseCase(
    private val authRepositorio: AuthRepositorio
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepositorio.iniciarSesion(email, password)
    }
}