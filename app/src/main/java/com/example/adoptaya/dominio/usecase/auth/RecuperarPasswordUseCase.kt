package com.example.adoptaya.dominio.usecase.auth

import com.example.adoptaya.dominio.repositorio.AuthRepositorio

class RecuperarPasswordUseCase(
    private val authRepositorio: AuthRepositorio
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepositorio.recuperarPassword(email)
    }
}