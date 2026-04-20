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
import com.example.adoptaya.data.remoto.MascotaServicio
import com.example.adoptaya.data.repositorio.MascotaRepositorioImpl
import com.example.adoptaya.dominio.usecase.ObtenerMascotaPorIdUseCase
import com.example.adoptaya.dominio.usecase.ObtenerMascotasUseCase
import com.example.adoptaya.presentacion.theme.AdoptaYaTheme
import com.example.adoptaya.presentacion.theme.ui.PantallaDePrueba
import com.example.adoptaya.presentacion.viewmodel.MascotaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val servicio = MascotaServicio()
        val repositorio = MascotaRepositorioImpl(servicio)
        val obtenerMascotasUC = ObtenerMascotasUseCase(repositorio)
        val obtenerPorIdUC = ObtenerMascotaPorIdUseCase(repositorio)

        val viewModel = MascotaViewModel(obtenerMascotasUC, obtenerPorIdUC)

        enableEdgeToEdge()
        setContent {
            AdoptaYaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaDePrueba(viewModel)
                }
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