package com.example.adoptaya.presentacion.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adoptaya.presentacion.ui.PantallaDetalleMascota
import com.example.adoptaya.presentacion.ui.PantallaFavoritos
import com.example.adoptaya.presentacion.ui.PantallaMapa
import com.example.adoptaya.presentacion.ui.PantallaMascotas
import com.example.adoptaya.presentacion.ui.PantallaNotificaciones
import com.example.adoptaya.presentacion.ui.PantallaPerfil
import com.example.adoptaya.presentacion.ui.PantallaPublicarMascota
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.NotificacionViewModel
import com.example.adoptaya.presentacion.viewmodel.PublicarMascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun Navegacion(
    mascotaViewModel: MascotaViewModel,
    favoritoViewModel: FavoritoViewModel,
    usuarioViewModel: UsuarioViewModel,
    notificacionViewModel: NotificacionViewModel,
    publicarMascotaViewModel: PublicarMascotaViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantallas.Mascotas.ruta
    ) {

        composable(Pantallas.Mascotas.ruta) {
            PantallaMascotas(
                mascotaViewModel = mascotaViewModel,
                alClickearMascota = { mascotaId ->
                    navController.navigate(
                        Pantallas.DetalleMascota.crearRuta(mascotaId)
                    )
                },
                alClickearFavorito = {
                    navController.navigate(Pantallas.Favoritos.ruta) { launchSingleTop = true }
                },
                alClickearPerfil = {
                    navController.navigate(
                        Pantallas.Perfil.crearRuta("1")
                    )
                },
                alClickearMapa = {
                    navController.navigate(Pantallas.Mapa.ruta) { launchSingleTop = true }
                },
                alClickearPublicar = {
                    navController.navigate(Pantallas.Publicar.ruta) { launchSingleTop = true }
                },
                alClickearNotificaciones = {
                    navController.navigate(Pantallas.Notificaciones.ruta) { launchSingleTop = true }
                }
            )
        }

        composable(Pantallas.DetalleMascota.ruta) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getString("mascotaId")

            PantallaDetalleMascota(
                mascotaId = mascotaId ?: "",
                mascotaViewModel = mascotaViewModel,
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Pantallas.Favoritos.ruta) {
            PantallaFavoritos(
                favoritoViewModel = favoritoViewModel,
                alClickearMascota = { mascotaId ->
                    navController.navigate(
                        Pantallas.DetalleMascota.crearRuta(mascotaId)
                    )
                },
                alClickearInicio = {
                    navController.navigate(Pantallas.Mascotas.ruta) { launchSingleTop = true }
                },
                alClickearMapa = {
                    navController.navigate(Pantallas.Mapa.ruta) { launchSingleTop = true }
                },
                alClickearPerfil = {
                    navController.navigate(
                        Pantallas.Perfil.crearRuta("1")
                    )
                },
                alClickearPublicar = {
                    navController.navigate(Pantallas.Publicar.ruta) { launchSingleTop = true }
                },
                alClickearNotificaciones = {
                    navController.navigate(Pantallas.Notificaciones.ruta) { launchSingleTop = true }
                }
            )
        }

        composable(Pantallas.Perfil.ruta) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId") ?: "1"
            PantallaPerfil(
                usuarioId = usuarioId,
                usuarioViewModel = usuarioViewModel,
                alClickearInicio = {
                    navController.navigate(Pantallas.Mascotas.ruta) { launchSingleTop = true }
                },
                alClickearFavorito = {
                    navController.navigate(Pantallas.Favoritos.ruta) { launchSingleTop = true }
                },
                alClickearMapa = {
                    navController.navigate(Pantallas.Mapa.ruta) { launchSingleTop = true }
                },
                alClickearPublicar = {
                    navController.navigate(Pantallas.Publicar.ruta) { launchSingleTop = true }
                },
                alClickearNotificaciones = {
                    navController.navigate(Pantallas.Notificaciones.ruta) { launchSingleTop = true }
                },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Pantallas.Mapa.ruta) {
            PantallaMapa(
                mascotaViewModel = mascotaViewModel,
                alClickearMascota = { mascotaId ->
                    navController.navigate(Pantallas.DetalleMascota.crearRuta(mascotaId)
                    )
                },
                alClickearInicio = {
                    navController.navigate(Pantallas.Mascotas.ruta) { launchSingleTop = true }
                },
                alClickearFavorito = {
                    navController.navigate(Pantallas.Favoritos.ruta) { launchSingleTop = true }
                },
                alClickearPerfil = {
                    navController.navigate(
                        Pantallas.Perfil.crearRuta("1")
                    )
                },
                alClickearPublicar = {
                    navController.navigate(Pantallas.Publicar.ruta) { launchSingleTop = true }
                },
                alClickearNotificaciones = {
                    navController.navigate(Pantallas.Notificaciones.ruta) { launchSingleTop = true }
                }
            )
        }

        composable(Pantallas.Notificaciones.ruta) {
            PantallaNotificaciones(
                notificacionViewModel = notificacionViewModel,
                alVolver = { navController.popBackStack() },
                alClickearMascota = { mascotaId ->
                    navController.navigate(
                        Pantallas.DetalleMascota.crearRuta(mascotaId)
                    )
                },
                alClickearPerfil = { usuarioId ->
                    navController.navigate(
                        Pantallas.Perfil.crearRuta(usuarioId)
                    )
                }
            )
        }

        composable(Pantallas.Publicar.ruta) {
            PantallaPublicarMascota(
                publicarMascotaViewModel = publicarMascotaViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}