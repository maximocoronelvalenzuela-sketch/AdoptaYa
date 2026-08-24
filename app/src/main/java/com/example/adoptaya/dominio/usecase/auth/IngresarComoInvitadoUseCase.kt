package com.example.adoptaya.dominio.usecase.auth

import com.example.adoptaya.dominio.repositorio.AuthRepositorio

class IngresarComoInvitadoUseCase(
    private val authRepositorio: AuthRepositorio
) {
    suspend operator fun invoke(): Result<String> {
        return authRepositorio.ingresarComoInvitado()
    }
}