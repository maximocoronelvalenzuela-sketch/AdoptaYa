package com.example.adoptaya.dominio.usecase

import com.example.adoptaya.R
import com.example.adoptaya.data.model.Enums
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.data.model.Usuario
import com.example.adoptaya.dominio.repositorio.MascotaRepositorio
import com.example.adoptaya.dominio.repositorio.UsuarioRepositorio

class InicializarDatosPruebaUseCase(
    private val mascotaRepositorio: MascotaRepositorio,
    private val usuarioRepositorio: UsuarioRepositorio,
    private val contexto: android.content.Context
) {
    suspend operator fun invoke() {
        // Si ya hay mascotas, cortamos la ejecución y no se pisa nada
        if (mascotaRepositorio.obtenerMascotas().isNotEmpty()) {
            return
        }

        val ahora = java.time.LocalDateTime.now()
        val fechaAyer = ahora.minusDays(1).format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val fechaHoy = ahora.minusHours(3).format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val paquete = "com.example.adoptaya"

        // Usuarios de prueba
        val usuario1 = Usuario(
            id = "1", email = "juan@mail.com",
            nombre = "Juan Pérez", telefono = "3851234567",
            latitud = -27.7833, longitud = -64.2667,
            provincia = "Santiago del Estero", ciudad = "Santiago del Estero",
            imagen = "android.resource://$paquete/drawable/perfil_juan", fechaHoraAlta = fechaAyer
        )
        val usuario2 = Usuario(
            id = "2", email = "maria@mail.com",
            nombre = "María Gómez", telefono = "3857654321",
            latitud = -27.8000, longitud = -64.2500,
            provincia = "Santiago del Estero", ciudad = "Santiago del Estero",
            imagen = "android.resource://$paquete/drawable/perfil_maria", fechaHoraAlta = fechaHoy
        )

        usuarioRepositorio.guardarUsuario(usuario1)
        usuarioRepositorio.guardarUsuario(usuario2)

        // Mascotas de prueba
        val mascota1 = Mascota(
            id = "1", nombre = "Max", tipo = Enums.TipoMascota.PERRO, edad = "3", tiempo = Enums.TiempoEdad.AÑOS, sexo = Enums.SexoMascota.MACHO,
            raza = "Beagle", tamaño = Enums.TamañoMascota.MEDIANO, esterilizado = true, vacunado = true, desparasitado = true,
            descripcionPersonalidad = listOf("Activo", "Sociable", "Cariñoso"),
            descripcionAdicional = "Ideal para familias con patio.", estado = Enums.EstadoMascota.DISPONIBLE,
            latitud = -27.7833, longitud = -64.2667, provincia = "Santiago del Estero", ciudad = "Santiago del Estero",
            barrio = "Centro", calle = "Mitre", numero = "123", imagenes = listOf("android.resource://$paquete/drawable/perro_max"),
            idUsuario = "1", fechaHoraAlta = fechaAyer
        )
        val mascota2 = Mascota(
            id = "2", nombre = "Luna", tipo = Enums.TipoMascota.GATO, edad = "1", tiempo = Enums.TiempoEdad.AÑOS, sexo = Enums.SexoMascota.HEMBRA,
            raza = null, tamaño = Enums.TamañoMascota.CHICO, esterilizado = true, vacunado = true, desparasitado = true,
            descripcionPersonalidad = listOf("Tranquilo", "Independiente"),
            descripcionAdicional = "Busca un hogar sin otros animales.", estado = Enums.EstadoMascota.DISPONIBLE,
            latitud = -27.8000, longitud = -64.2500, provincia = "Santiago del Estero", ciudad = "Santiago del Estero",
            barrio = "Belgrano", calle = "Av. Libertador", numero = "456", imagenes = listOf("android.resource://$paquete/drawable/gato_luna"),
            idUsuario = "2", fechaHoraAlta = fechaHoy
        )

        mascotaRepositorio.guardarMascota(mascota1)
        mascotaRepositorio.guardarMascota(mascota2)
    }
}