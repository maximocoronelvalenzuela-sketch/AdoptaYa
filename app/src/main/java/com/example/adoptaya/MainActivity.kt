package com.example.adoptaya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.adoptaya.data.repositorio.FavoritoRepositorioImpl
import com.example.adoptaya.data.repositorio.MascotaRepositorioImpl
import com.example.adoptaya.data.repositorio.NotificacionRepositorioImpl
import com.example.adoptaya.data.repositorio.UsuarioRepositorioImpl
import com.example.adoptaya.dominio.usecase.InicializarDatosPruebaUseCase
import com.example.adoptaya.dominio.usecase.favorito.AgregarFavoritoUseCase
import com.example.adoptaya.dominio.usecase.favorito.EliminarFavoritoUseCase
import com.example.adoptaya.dominio.usecase.favorito.ObtenerFavoritosPorUsuarioUseCase
import com.example.adoptaya.dominio.usecase.mascota.GuardarMascotaUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotaPorIdUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotasUseCase
import com.example.adoptaya.dominio.usecase.notificacion.GuardarNotificacionUseCase
import com.example.adoptaya.dominio.usecase.notificacion.ObtenerNotificacionesPorUsuario
import com.example.adoptaya.dominio.usecase.usuario.ObtenerUsuarioPorIdUseCase
import com.example.adoptaya.presentacion.navegacion.Navegacion
import com.example.adoptaya.presentacion.theme.AdoptaYaTheme
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.NotificacionViewModel
import com.example.adoptaya.presentacion.viewmodel.PublicarMascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as AdoptayaApplication

        val mascotaDao = app.database.mascotaDao()
        val usuarioDao = app.database.usuarioDao()
        val favoritoDao = app.database.favoritoDao()
        val notificacionDao = app.database.notificacionDao()

        val mascotaRepositorio = MascotaRepositorioImpl(mascotaDao)
        val usuarioRepositorio = UsuarioRepositorioImpl(usuarioDao)
        val favoritoRepositorio = FavoritoRepositorioImpl(favoritoDao)
        val notificacionRepositorio = NotificacionRepositorioImpl(notificacionDao)

        val obtenerMascotasUC = ObtenerMascotasUseCase(mascotaRepositorio)
        val obtenerPorIdUC = ObtenerMascotaPorIdUseCase(mascotaRepositorio)
        val guardarMascotaUC = GuardarMascotaUseCase(mascotaRepositorio)
        val obtenerUsuarioPorIdUC = ObtenerUsuarioPorIdUseCase(usuarioRepositorio)
        val obtenerFavoritosPorUsuarioUC = ObtenerFavoritosPorUsuarioUseCase(favoritoRepositorio, mascotaRepositorio)
        val agregarFavoritoUseCase = AgregarFavoritoUseCase(favoritoRepositorio)
        val eliminarFavoritoUseCase = EliminarFavoritoUseCase(favoritoRepositorio)
        val obtenerNotificacionesPorUsuarioUC = ObtenerNotificacionesPorUsuario(notificacionRepositorio)
        val guardarNotificacionUC = GuardarNotificacionUseCase(notificacionRepositorio)

        val mascotaViewModel = MascotaViewModel(obtenerMascotasUC, obtenerPorIdUC)
        val usuarioViewModel = UsuarioViewModel(obtenerUsuarioPorIdUC)
        val favoritoViewModel = FavoritoViewModel(obtenerFavoritosPorUsuarioUC, agregarFavoritoUseCase, eliminarFavoritoUseCase)
        val notificacionViewModel = NotificacionViewModel(obtenerNotificacionesPorUsuarioUC, guardarNotificacionUC)
        val publicarMascotaViewModel = PublicarMascotaViewModel(guardarMascotaUC)

        val inicializarDatosPruebaUC = InicializarDatosPruebaUseCase(mascotaRepositorio, usuarioRepositorio)

        lifecycleScope.launch {
            inicializarDatosPruebaUC() // Carga los datos
        }

        enableEdgeToEdge()
        setContent {
            Navegacion(
                mascotaViewModel = mascotaViewModel,
                favoritoViewModel = favoritoViewModel,
                usuarioViewModel = usuarioViewModel,
                notificacionViewModel = notificacionViewModel,
                publicarMascotaViewModel = publicarMascotaViewModel
            )
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