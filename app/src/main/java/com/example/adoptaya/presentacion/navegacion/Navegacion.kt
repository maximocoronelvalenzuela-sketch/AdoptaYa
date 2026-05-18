package com.example.adoptaya.presentacion.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adoptaya.presentacion.theme.ui.PantallaDetalleMascota
import com.example.adoptaya.presentacion.theme.ui.PantallaFavoritos
import com.example.adoptaya.presentacion.theme.ui.PantallaMapa
import com.example.adoptaya.presentacion.theme.ui.PantallaMascotas
import com.example.adoptaya.presentacion.theme.ui.PantallaPerfil
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

@Composable
fun Navegacion(
    mascotaViewModel: MascotaViewModel,
    favoritoViewModel: FavoritoViewModel,
    usuarioViewModel: UsuarioViewModel
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
                    navController.navigate(Pantallas.Favoritos.ruta)
                },
                alClickearPerfil = {
                    navController.navigate(Pantallas.Perfil.ruta)
                },
                alClickearMapa = {
                    navController.navigate(Pantallas.Mapa.ruta)
                }
            )
        }

        composable(Pantallas.DetalleMascota.ruta) { backStackEntry ->

            val mascotaId =
                backStackEntry.arguments?.getString("mascotaId")

            PantallaDetalleMascota(
                mascotaId = mascotaId ?: "",
                mascotaViewModel = mascotaViewModel
            )
        }

        composable(Pantallas.Favoritos.ruta) {

            PantallaFavoritos(
                favoritoViewModel = favoritoViewModel,
                alClickearMascota = { mascotaId ->
                    navController.navigate(
                        Pantallas.DetalleMascota.crearRuta(mascotaId)
                    )
                }
            )
        }

        composable(Pantallas.Perfil.ruta) {
            PantallaPerfil(
                usuarioViewModel = usuarioViewModel
            )
        }

        composable(Pantallas.Mapa.ruta) {
            PantallaMapa(
                mascotaViewModel = mascotaViewModel,
                alClickearMascota = { mascotaId ->
                    navController.navigate(Pantallas.DetalleMascota.crearRuta(mascotaId)
                    )
                }
            )
        }
    }
}