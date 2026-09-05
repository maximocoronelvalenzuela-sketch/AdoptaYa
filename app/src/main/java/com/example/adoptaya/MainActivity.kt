package com.example.adoptaya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.adoptaya.data.repositorio.AuthRepositorioImpl
import com.example.adoptaya.data.repositorio.FavoritoRepositorioImpl
import com.example.adoptaya.data.repositorio.MascotaRepositorioImpl
import com.example.adoptaya.data.repositorio.NotificacionRepositorioImpl
import com.example.adoptaya.data.repositorio.UsuarioRepositorioImpl
import com.example.adoptaya.dominio.usecase.InicializarDatosPruebaUseCase
import com.example.adoptaya.dominio.usecase.auth.CerrarSesionUseCase
import com.example.adoptaya.dominio.usecase.auth.IngresarComoInvitadoUseCase
import com.example.adoptaya.dominio.usecase.auth.IniciarSesionUseCase
import com.example.adoptaya.dominio.usecase.auth.ObtenerIdUsuarioActualUseCase
import com.example.adoptaya.dominio.usecase.auth.RecuperarPasswordUseCase
import com.example.adoptaya.dominio.usecase.auth.RegistrarUseCase
import com.example.adoptaya.dominio.usecase.favorito.AgregarFavoritoUseCase
import com.example.adoptaya.dominio.usecase.favorito.EliminarFavoritoUseCase
import com.example.adoptaya.dominio.usecase.favorito.ObtenerFavoritosPorUsuarioUseCase
import com.example.adoptaya.dominio.usecase.mascota.ActualizarEstadoPublicacionUseCase
import com.example.adoptaya.dominio.usecase.mascota.DarDeBajaMascotaUseCase
import com.example.adoptaya.dominio.usecase.mascota.GuardarMascotaUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotaPorIdUseCase
import com.example.adoptaya.dominio.usecase.mascota.ObtenerMascotasUseCase
import com.example.adoptaya.dominio.usecase.notificacion.GuardarNotificacionUseCase
import com.example.adoptaya.dominio.usecase.notificacion.ObtenerNotificacionesPorUsuario
import com.example.adoptaya.dominio.usecase.usuario.ActualizarUsuarioUseCase
import com.example.adoptaya.dominio.usecase.usuario.CrearUsuarioUseCase
import com.example.adoptaya.dominio.usecase.usuario.ObtenerUsuarioPorIdUseCase
import com.example.adoptaya.dominio.usecase.usuario.VerificarPerfilIncompletoUseCase
import com.example.adoptaya.presentacion.navegacion.Navegacion
import com.example.adoptaya.presentacion.theme.AdoptaYaTheme
import com.example.adoptaya.presentacion.viewmodel.AuthViewModel
import com.example.adoptaya.presentacion.viewmodel.FavoritoViewModel
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.NotificacionViewModel
import com.example.adoptaya.presentacion.viewmodel.PublicarMascotaViewModel
import com.example.adoptaya.presentacion.viewmodel.UsuarioViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as AdoptayaApplication
        val firebaseAuth = FirebaseAuth.getInstance()

        val mascotaDao = app.database.mascotaDao()
        val usuarioDao = app.database.usuarioDao()
        val favoritoDao = app.database.favoritoDao()
        val notificacionDao = app.database.notificacionDao()

        val mascotaRepositorio = MascotaRepositorioImpl(mascotaDao)
        val usuarioRepositorio = UsuarioRepositorioImpl(usuarioDao)
        val favoritoRepositorio = FavoritoRepositorioImpl(favoritoDao)
        val notificacionRepositorio = NotificacionRepositorioImpl(notificacionDao)
        val authRepositorio = AuthRepositorioImpl(firebaseAuth)

        val obtenerMascotasUC = ObtenerMascotasUseCase(mascotaRepositorio)
        val obtenerPorIdUC = ObtenerMascotaPorIdUseCase(mascotaRepositorio)
        val guardarMascotaUC = GuardarMascotaUseCase(mascotaRepositorio)
        val obtenerUsuarioPorIdUC = ObtenerUsuarioPorIdUseCase(usuarioRepositorio)
        val obtenerFavoritosPorUsuarioUC = ObtenerFavoritosPorUsuarioUseCase(favoritoRepositorio, mascotaRepositorio)
        val agregarFavoritoUC = AgregarFavoritoUseCase(favoritoRepositorio)
        val eliminarFavoritoUC = EliminarFavoritoUseCase(favoritoRepositorio)
        val obtenerNotificacionesPorUsuarioUC = ObtenerNotificacionesPorUsuario(notificacionRepositorio)
        val guardarNotificacionUC = GuardarNotificacionUseCase(notificacionRepositorio)
        val iniciarSesionUC = IniciarSesionUseCase(authRepositorio)
        val registrarUC = RegistrarUseCase(authRepositorio)
        val obtenerIdUsuarioActualUC = ObtenerIdUsuarioActualUseCase(authRepositorio)
        val crearUsuarioUC = CrearUsuarioUseCase(usuarioRepositorio)
        val cerrarSesionUC = CerrarSesionUseCase(authRepositorio)
        val verificarPerfilIncompletoUC = VerificarPerfilIncompletoUseCase()
        val actualizarUsuarioUC = ActualizarUsuarioUseCase(usuarioRepositorio)
        val recuperarPasswordUC = RecuperarPasswordUseCase(authRepositorio)
        val darDeAltaInvitadoUC = DarDeBajaMascotaUseCase(mascotaRepositorio)
        val actualizarEstadoPublicacionUC = ActualizarEstadoPublicacionUseCase(mascotaRepositorio)

        val mascotaViewModel = MascotaViewModel(obtenerMascotasUC, obtenerPorIdUC, darDeAltaInvitadoUC, actualizarEstadoPublicacionUC)
        val usuarioViewModel = UsuarioViewModel(obtenerUsuarioPorIdUC, verificarPerfilIncompletoUC, actualizarUsuarioUC)
        val favoritoViewModel = FavoritoViewModel(obtenerFavoritosPorUsuarioUC, agregarFavoritoUC, eliminarFavoritoUC)
        val notificacionViewModel = NotificacionViewModel(obtenerNotificacionesPorUsuarioUC, guardarNotificacionUC)
        val publicarMascotaViewModel = PublicarMascotaViewModel(guardarMascotaUC, obtenerIdUsuarioActualUC)
        val authViewModel = AuthViewModel(iniciarSesionUC, registrarUC, obtenerIdUsuarioActualUC, crearUsuarioUC, cerrarSesionUC, recuperarPasswordUC)

        val inicializarDatosPruebaUC = InicializarDatosPruebaUseCase(mascotaRepositorio, usuarioRepositorio, this)

        enableEdgeToEdge()
        setContent {
            var datosInicializados by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                inicializarDatosPruebaUC()
                datosInicializados = true
            }

            // Para solucionar la condicion de carrera
            if (!datosInicializados) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFE85A13))
                }
            } else {
                Navegacion(
                    mascotaViewModel = mascotaViewModel,
                    favoritoViewModel = favoritoViewModel,
                    usuarioViewModel = usuarioViewModel,
                    notificacionViewModel = notificacionViewModel,
                    publicarMascotaViewModel = publicarMascotaViewModel,
                    authViewModel = authViewModel
                )
            }
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