package com.example.adoptaya.data.repositorio

import android.util.Log
import com.example.adoptaya.data.local.dao.UsuarioDao
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.data.remoto.RetrofitClient
import com.example.adoptaya.dominio.repositorio.UsuarioRepositorio

class UsuarioRepositorioImpl(
    private val usuarioDao: UsuarioDao
) : UsuarioRepositorio {

    private val api = RetrofitClient.apiService
    private val TAG = "API_ADOPTAYA_USUARIOS"

    override suspend fun obtenerUsuarioPorId(id: String): Usuario? {
        val usuarioLocal = usuarioDao.obtenerPorId(id)
        if (usuarioLocal != null) return usuarioLocal

        try {
            val response = api.obtenerUsuarioPorId(id)
            if (response.isSuccessful && response.body() != null) {
                val usuarioRemoto = response.body()!!
                // Guarda en local para cachear y evitar crashes
                usuarioDao.insertarUsuario(usuarioRemoto)
                return usuarioRemoto
            }
        } catch (e: Exception) {
            Log.e(TAG, "Falla de red en obtenerUsuarioPorId: ${e.message}", e)
        }
        return usuarioDao.obtenerPorId(id)
    }

    override suspend fun guardarUsuario(usuario: Usuario) {
        // Guarda localmente
        usuarioDao.insertarUsuario(usuario)
        try {
            // Envia el usuario al backend en la nube
            api.guardarUsuario(usuario)
        } catch (e: Exception) {
            Log.e(TAG, "No se pudo sincronizar el usuario con el backend: ${e.message}", e)
        }
    }
}