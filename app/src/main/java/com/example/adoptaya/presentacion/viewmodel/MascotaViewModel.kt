package com.example.adoptaya.presentacion.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.dominio.usecase.ObtenerMascotasUseCase

class MascotaViewModel (
    private val obtenerMascotasUseCase: ObtenerMascotasUseCase
) : ViewModel() {
    private val _mascotas = mutableStateOf<List<Mascota>>(emptyList())
    val mascotas: State<List<Mascota>> = _mascotas

    fun cargarMascotas(){
        _mascotas.value = obtenerMascotasUseCase()
    }
}