package com.example.adoptaya.dominio.repositorio

interface AuthRepositorio {
    suspend fun iniciarSesion(email: String, password: String): Result<String>

    suspend fun registrar(nombre: String, email: String, password: String): Result<String>

    suspend fun ingresarComoInvitado(): Result<String>

    suspend fun recuperarPassword(email: String): Result<Unit>

    fun obtenerIdUsuarioActual(): String?

    fun cerrarSesion()
}