package com.example.adoptaya.dominio.usecase

import com.example.adoptaya.data.model.EstadoMascota
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio
import com.example.adoptaya.dominio.repositorio.UsuarioRepositorio

class InicializarDatosPruebaUseCase(
    private val mascotaRepositorio: MascotaRepositorio,
    private val usuarioRepositorio: UsuarioRepositorio
) {
    suspend operator fun invoke() {
        // Si ya hay mascotas, cortamos la ejecución y no se pisa nada
        if (mascotaRepositorio.obtenerMascotas().isNotEmpty()) {
            return
        }

        // Usuarios de prueba
        val usuario1 = Usuario(
            id = "1", email = "juan@mail.com", password = "123456",
            nombre = "Juan Pérez", telefono = "3851234567",
            latitud = -27.7833, longitud = -64.2667,
            provincia = "Santiago del Estero", ciudad = "Santiago del Estero",
            barrio = "Centro", imagen = null, fechaHoraAlta = "2024-01-01T10:00:00"
        )
        val usuario2 = Usuario(
            id = "2", email = "maria@mail.com", password = "123456",
            nombre = "María Gómez", telefono = "3857654321",
            latitud = -27.8000, longitud = -64.2500,
            provincia = "Santiago del Estero", ciudad = "Santiago del Estero",
            barrio = "Belgrano", imagen = null, fechaHoraAlta = "2024-01-02T11:00:00"
        )

        usuarioRepositorio.guardarUsuario(usuario1)
        usuarioRepositorio.guardarUsuario(usuario2)

        // Mascotas de prueba
        val mascota1 = Mascota(
            id = "1", nombre = "Max", tipo = "Perro", edad = "3", sexo = "Macho",
            raza = "Beagle", tamano = "Mediano", esterilizado = true, vacunado = true, desparasitado = true,
            descripcionPersonalidad = listOf("Activo", "Sociable", "Cariñoso"),
            descripcionAdicional = "Ideal para familias con patio.", estado = EstadoMascota.DISPONIBLE,
            latitud = -27.7833, longitud = -64.2667, provincia = "Santiago del Estero", ciudad = "Santiago del Estero",
            barrio = "Centro", imagenes = listOf("https://images.unsplash.com/photo-1543466835-00a7907e9de1"),
            idUsuario = "1", fechaHoraAlta = "2024-06-01T12:00:00"
        )
        val mascota2 = Mascota(
            id = "2", nombre = "Luna", tipo = "Gato", edad = "1", sexo = "Hembra",
            raza = null, tamano = "Chico", esterilizado = true, vacunado = true, desparasitado = true,
            descripcionPersonalidad = listOf("Tranquilo", "Independiente"),
            descripcionAdicional = "Busca un hogar sin otros animales.", estado = EstadoMascota.DISPONIBLE,
            latitud = -27.8000, longitud = -64.2500, provincia = "Santiago del Estero", ciudad = "Santiago del Estero",
            barrio = "Belgrano", imagenes = listOf("https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba"),
            idUsuario = "2", fechaHoraAlta = "2024-06-02T10:30:00"
        )

        mascotaRepositorio.guardarMascota(mascota1)
        mascotaRepositorio.guardarMascota(mascota2)
    }
}