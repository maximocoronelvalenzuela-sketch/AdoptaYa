package com.example.adoptaya

import android.app.Application
import androidx.room.Room
import com.example.adoptaya.data.local.AdoptaYaDatabase

class AdoptayaApplication : Application() {

    // Unica instancia de la base de datos para toda la app
    val database by lazy {
        Room.databaseBuilder(
            this,
            AdoptaYaDatabase::class.java,
            "adoptaya_database"
        ).build()
    }
}