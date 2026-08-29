package com.example.adoptaya.data.repositorio

import com.google.firebase.auth.FirebaseAuth
import com.example.adoptaya.dominio.repositorio.AuthRepositorio
import kotlinx.coroutines.tasks.await

class AuthRepositorioImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepositorio {

    override suspend fun iniciarSesion(email: String, password: String): Result<String> {
        return try {
            val resultado = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(resultado.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registrar(nombre: String, email: String, password: String, telefono: String): Result<String> {
        return try {
            val resultado = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(resultado.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun ingresarComoInvitado(): Result<String> {
        return try {
            val resultado = firebaseAuth.signInAnonymously().await()
            Result.success(resultado.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun recuperarPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun obtenerIdUsuarioActual(): String? {
        // Devuelve null si no hay nadie logueado
        return firebaseAuth.currentUser?.uid
    }

    override fun cerrarSesion() {
        firebaseAuth.signOut()
    }
}