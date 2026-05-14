package com.example.adoptaya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.adoptaya.data.remoto.FavoritoServicio
import com.example.adoptaya.data.remoto.MascotaServicio
import com.example.adoptaya.data.remoto.NotificacionServicio
import com.example.adoptaya.data.remoto.UsuarioServicio
import com.example.adoptaya.data.repositorio.FavoritoRepositorioImpl
import com.example.adoptaya.data.repositorio.MascotaRepositorioImpl
import com.example.adoptaya.data.repositorio.NotificacionRepositorioImpl
import com.example.adoptaya.data.repositorio.UsuarioRepositorioImpl
import com.example.adoptaya.dominio.usecase.favorito.ObtenerFavoritosPorUsuarioUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotaPorIdUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotasUseCase
import com.example.adoptaya.dominio.usecase.notificacion.ObtenerNotificacionesPorUsuario
import com.example.adoptaya.dominio.usecase.usuario.ObtenerUsuarioPorIdUseCase
import com.example.adoptaya.presentacion.navegacion.Navegacion
import com.example.adoptaya.presentacion.theme.AdoptaYaTheme
import com.example.adoptaya.presentacion.theme.ui.PantallaDePrueba
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.NotificacionViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mascotaServicio = MascotaServicio()
        val usuarioServicio = UsuarioServicio()
        val favoritoServicio = FavoritoServicio()
        val notificacionServicio = NotificacionServicio()

        val mascotaRepositorio = MascotaRepositorioImpl(mascotaServicio)
        val usuarioRepositorio = UsuarioRepositorioImpl(usuarioServicio)
        val favoritoRepositorio = FavoritoRepositorioImpl(favoritoServicio)
        val notificacionRepositorio = NotificacionRepositorioImpl(notificacionServicio)

        val obtenerMascotasUC = ObtenerMascotasUseCase(mascotaRepositorio)
        val obtenerPorIdUC = ObtenerMascotaPorIdUseCase(mascotaRepositorio)
        val obtenerUsuarioPorIdUC = ObtenerUsuarioPorIdUseCase(usuarioRepositorio)
        val obtenerFavoritosPorUsuarioUC = ObtenerFavoritosPorUsuarioUseCase(favoritoRepositorio, mascotaRepositorio)
        val obtenerNotificacionesPorUsuarioUC = ObtenerNotificacionesPorUsuario(notificacionRepositorio)

        val mascotaViewModel = MascotaViewModel(obtenerMascotasUC, obtenerPorIdUC)
        val usuarioViewModel = UsuarioViewModel(obtenerUsuarioPorIdUC)
        val favoritoViewModel = FavoritoViewModel(obtenerFavoritosPorUsuarioUC)
        val notificacionViewModel = NotificacionViewModel(obtenerNotificacionesPorUsuarioUC)

        enableEdgeToEdge()
        setContent {
            Navegacion(
                mascotaViewModel = mascotaViewModel
            )

//            AdoptaYaTheme {
//                Surface(modifier = Modifier.fillMaxSize()) {
//                    PantallaDePrueba(mascotaViewModel, usuarioViewModel, favoritoViewModel, notificacionViewModel)
//                }
//            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdoptaYaTheme {
        Greeting("Android")
    }
}