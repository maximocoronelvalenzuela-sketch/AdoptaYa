package com.example.adoptaya.dominio.usecase.auth

import com.example.adoptaya.dominio.repositorio.AuthRepositorio

class RegistrarUseCase(
    private val authRepositorio: AuthRepositorio
) {
    suspend operator fun invoke(nombre: String, email: String, password: String, telefono: String): Result<String> {
        return authRepositorio.registrar(nombre, email, password, telefono)
    }
}