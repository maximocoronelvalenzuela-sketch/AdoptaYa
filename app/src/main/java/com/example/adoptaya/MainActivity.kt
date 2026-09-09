package com.example.adoptaya

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.data.remoto.RetrofitClient
import com.example.adoptaya.data.repositorio.AuthRepositorioImpl
import com.example.adoptaya.data.repositorio.FavoritoRepositorioImpl
import com.example.adoptaya.data.repositorio.MascotaRepositorioImpl
import com.example.adoptaya.data.repositorio.NotificacionRepositorioImpl
import com.example.adoptaya.data.repositorio.UsuarioRepositorioImpl
import com.example.adoptaya.dominio.usecase.auth.CerrarSesionUseCase
import com.example.adoptaya.dominio.usecase.auth.IniciarSesionUseCase
import com.example.adoptaya.dominio.usecase.auth.ObtenerIdUsuarioActualUseCase
import com.example.adoptaya.dominio.usecase.auth.RecuperarPasswordUseCase
import com.example.adoptaya.dominio.usecase.auth.RegistrarUseCase
import com.example.adoptaya.dominio.usecase.favorito.AgregarFavoritoUseCase
import com.example.adoptaya.dominio.usecase.favorito.EliminarFavoritoUseCase
import com.example.adoptaya.dominio.usecase.favorito.ObtenerFavoritosPorUsuarioUseCase
import com.example.adoptaya.dominio.usecase.mascota.ActualizarEstadoPublicacionUseCase
import com.example.adoptaya.dominio.usecase.mascota.DarDeBajaMascotaUseCase
import com.example.adoptaya.dominio.usecase.mascota.EditarMascotaUseCase
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var irANotificaciones by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        irANotificaciones = intent?.getBooleanExtra("abrir_notificaciones", false) ?: false

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
        val obtenerMascotaPorIdUC = ObtenerMascotaPorIdUseCase(mascotaRepositorio)
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
        val editarMascotaUC = EditarMascotaUseCase(mascotaRepositorio)

        val mascotaViewModel = MascotaViewModel(obtenerMascotasUC, obtenerMascotaPorIdUC, darDeAltaInvitadoUC, actualizarEstadoPublicacionUC)
        val usuarioViewModel = UsuarioViewModel(obtenerUsuarioPorIdUC, verificarPerfilIncompletoUC, actualizarUsuarioUC)
        val favoritoViewModel = FavoritoViewModel(obtenerFavoritosPorUsuarioUC, agregarFavoritoUC, eliminarFavoritoUC)
        val notificacionViewModel = NotificacionViewModel(obtenerNotificacionesPorUsuarioUC, guardarNotificacionUC)
        val publicarMascotaViewModel = PublicarMascotaViewModel(guardarMascotaUC, obtenerIdUsuarioActualUC, obtenerMascotaPorIdUC, editarMascotaUC)
        val authViewModel = AuthViewModel(iniciarSesionUC, registrarUC, obtenerIdUsuarioActualUC, crearUsuarioUC, cerrarSesionUC, recuperarPasswordUC)


        enableEdgeToEdge()
        setContent {
            val idActual = authViewModel.idUsuarioActual

            // BUCLE DE POLLING ATADO AL CICLO DE VIDA
            LaunchedEffect(idActual) {
                if (idActual != null) {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            while (true) {
                                try {
                                    val response = RetrofitClient.apiService.sincronizarNotificaciones(idActual)
                                    if (response.isSuccessful && response.body() != null) {
                                        val notifsRemotas = response.body()!!
                                        for (noti in notifsRemotas) {
                                            if (!notificacionDao.existeNotificacion(noti.id)) {
                                                notificacionDao.insertarNotificacion(noti)
                                                // Refresca la lista visual
                                                notificacionViewModel.cargarNotificacionesDeUsuario(idActual)
                                                // Dispara la alerta en el celular
                                                mostrarNotificacionSO(this@MainActivity, noti)
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    // Silencioso: si falla el internet, vuelve a intentar en 10s
                                }
                                delay(10000) // 10 segundos
                            }
                        }
                    }
                }
            }

            Navegacion(
                mascotaViewModel = mascotaViewModel,
                favoritoViewModel = favoritoViewModel,
                usuarioViewModel = usuarioViewModel,
                notificacionViewModel = notificacionViewModel,
                publicarMascotaViewModel = publicarMascotaViewModel,
                authViewModel = authViewModel,
                irANotificaciones = irANotificaciones,
                onNavegacionConsumida = { irANotificaciones = true }
            )
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        if (intent.getBooleanExtra("abrir_notificaciones", true)) {
            irANotificaciones = true
        }
    }
}

private fun mostrarNotificacionSO(contexto: Context, notificacion: Notificacion) {
    val canalId = "canal_adoptaya_defecto"
    val notificationManager = contexto.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val canal = NotificationChannel(canalId, "Notificaciones Generales", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(canal)
    }

    val intent = android.content.Intent(contexto, MainActivity::class.java).apply {
        flags = android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra("abrir_notificaciones", true)
    }

    val pendingIntent = android.app.PendingIntent.getActivity(
        contexto,
        0, // No necesitamos el ID aquí para que no abra el modal
        intent,
        android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(contexto, canalId)
        .setSmallIcon(R.drawable.ic_notificacion_silueta)
        .setColor(android.graphics.Color.parseColor("#F28B2A"))
        .setContentTitle(notificacion.titulo)
        .setContentText(notificacion.descripcion)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    notificationManager.notify(notificacion.id.hashCode(), builder.build())
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