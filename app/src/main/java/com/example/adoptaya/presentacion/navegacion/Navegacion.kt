package com.example.adoptaya.presentacion.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adoptaya.presentacion.ui.PantallaDetalleMascota
import com.example.adoptaya.presentacion.ui.PantallaEditarPerfil
import com.example.adoptaya.presentacion.ui.PantallaFavoritos
import com.example.adoptaya.presentacion.ui.PantallaLogin
import com.example.adoptaya.presentacion.ui.PantallaMapa
import com.example.adoptaya.presentacion.ui.PantallaMascotas
import com.example.adoptaya.presentacion.ui.PantallaNotificaciones
import com.example.adoptaya.presentacion.ui.PantallaPerfil
import com.example.adoptaya.presentacion.ui.PantallaPublicarMascota
import com.example.adoptaya.presentacion.ui.PantallaRecuperarPassword
import com.example.adoptaya.presentacion.ui.PantallaRegistro
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
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
    publicarMascotaViewModel: PublicarMascotaViewModel,
    authViewModel: AuthViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantallas.Mascotas.ruta
    ) {

        composable(Pantallas.Mascotas.ruta) {
            PantallaMascotas(
                mascotaViewModel = mascotaViewModel,
                favoritoViewModel = favoritoViewModel,
                authViewModel = authViewModel,
                usuarioViewModel = usuarioViewModel,
                alClickearMascota = { mascotaId ->
                    navController.navigate(
                        Pantallas.DetalleMascota.crearRuta(mascotaId)
                    )
                },
                alClickearFavorito = {
                    navController.navigate(Pantallas.Favoritos.ruta) { launchSingleTop = true }
                },
                alClickearPerfil = {
                    authViewModel.idUsuarioActual?.let { miId ->
                        navController.navigate(Pantallas.Perfil.crearRuta(miId))
                    }
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
                alClickearPerfilDueño = { usuarioId ->
                    navController.navigate(
                        Pantallas.Perfil.crearRuta(usuarioId)
                    )
                },
                alNavegarLogin = {
                    navController.navigate(Pantallas.Login.ruta)
                }
            )
        }

        composable(Pantallas.DetalleMascota.ruta) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getString("mascotaId")

            PantallaDetalleMascota(
                mascotaId = mascotaId ?: "",
                mascotaViewModel = mascotaViewModel,
                favoritoViewModel = favoritoViewModel,
                notificacionViewModel = notificacionViewModel,
                authViewModel = authViewModel,
                alVolver = { navController.popBackStack() },
                alNavegarLogin = {
                    navController.navigate(Pantallas.Login.ruta)
                }
            )
        }

        composable(Pantallas.Favoritos.ruta) {
            PantallaFavoritos(
                favoritoViewModel = favoritoViewModel,
                usuarioViewModel = usuarioViewModel,
                authViewModel = authViewModel,
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
                    authViewModel.idUsuarioActual?.let { miId ->
                        navController.navigate(Pantallas.Perfil.crearRuta(miId))
                    }
                },
                alClickearPublicar = {
                    navController.navigate(Pantallas.Publicar.ruta) { launchSingleTop = true }
                },
                alClickearNotificaciones = {
                    navController.navigate(Pantallas.Notificaciones.ruta) { launchSingleTop = true }
                },
                alClickearPerfilDueño = { usuarioId ->
                    navController.navigate(
                        Pantallas.Perfil.crearRuta(usuarioId)
                    )
                }
            )
        }

        composable(Pantallas.Perfil.ruta) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId") ?: "1"
            PantallaPerfil(
                usuarioId = usuarioId,
                usuarioViewModel = usuarioViewModel,
                mascotaViewModel = mascotaViewModel,
                favoritoViewModel = favoritoViewModel,
                authViewModel = authViewModel,
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
                alVolver = { navController.popBackStack() },
                alCerrarSesion = {
                    navController.navigate(Pantallas.Mascotas.ruta) {
                        popUpTo(0)
                    }
                },
                alClickearMascota = { mascotaId ->
                    navController.navigate(Pantallas.DetalleMascota.crearRuta(mascotaId))
                },
                alNavegarLogin = {
                    navController.navigate(Pantallas.Login.ruta)
                },
                alEditarPerfil = {
                    navController.navigate(Pantallas.EditarPerfil.ruta)
                }
            )
        }

        composable(Pantallas.Mapa.ruta) {
            PantallaMapa(
                mascotaViewModel = mascotaViewModel,
                authViewModel = authViewModel,
                usuarioViewModel = usuarioViewModel,
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
                    authViewModel.idUsuarioActual?.let { miId ->
                        navController.navigate(Pantallas.Perfil.crearRuta(miId))
                    }
                },
                alClickearPublicar = {
                    navController.navigate(Pantallas.Publicar.ruta) { launchSingleTop = true }
                },
                alClickearNotificaciones = {
                    navController.navigate(Pantallas.Notificaciones.ruta) { launchSingleTop = true }
                },
                alNavegarLogin = {
                    navController.navigate(Pantallas.Login.ruta)
                }
            )
        }

        composable(Pantallas.Notificaciones.ruta) {
            PantallaNotificaciones(
                notificacionViewModel = notificacionViewModel,
                usuarioViewModel = usuarioViewModel,
                authViewModel = authViewModel,
                alVolver = {
                    navController.popBackStack()
                },
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

        composable(Pantallas.Login.ruta) {
            PantallaLogin(
                authViewModel = authViewModel,
                alNavegarRegistro = { navController.navigate(Pantallas.Registro.ruta) },
                alNavegarRecuperarPassword = { navController.navigate(Pantallas.RecuperarPassword.ruta) },
                alIngresarComoInvitado = {
                    authViewModel.ingresarComoInvitado(
                        onExito = {
                            navController.navigate(Pantallas.Mascotas.ruta) {
                                popUpTo(Pantallas.Login.ruta) { inclusive = true }
                            }
                        }
                    )
                },
                alIniciarSesion = { email, password ->
                    authViewModel.iniciarSesion(
                        email = email,
                        password = password,
                        onExito = {
                            navController.navigate(Pantallas.Mascotas.ruta) {
                                popUpTo(Pantallas.Login.ruta) { inclusive = true }
                            }
                        }
                    )
                }
            )
        }

        composable(Pantallas.Registro.ruta) {
            PantallaRegistro(
                authViewModel = authViewModel,
                alNavegarLogin = {
                    navController.navigate(Pantallas.Login.ruta) {
                        popUpTo(Pantallas.Registro.ruta) { inclusive = true }
                    }
                },
                alRegistrar = { nombre, email, password, telefono ->
                    authViewModel.registrar(
                        nombre = nombre,
                        email = email,
                        password = password,
                        telefono = telefono,
                        onExito = {
                            navController.navigate(Pantallas.Mascotas.ruta) {
                                popUpTo(Pantallas.Login.ruta) { inclusive = true }
                                popUpTo(Pantallas.Registro.ruta) { inclusive = true }
                            }
                        }
                    )
                }
            )
        }

        composable(Pantallas.RecuperarPassword.ruta) {
            PantallaRecuperarPassword(
                authViewModel = authViewModel,
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Pantallas.EditarPerfil.ruta) {
            PantallaEditarPerfil(
                usuarioViewModel = usuarioViewModel,
                alVolver = { navController.popBackStack() }
            )
        }
    }
}