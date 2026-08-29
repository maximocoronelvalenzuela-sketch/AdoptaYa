package com.example.adoptaya.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.adoptaya.data.local.dao.FavoritoDao
import com.example.adoptaya.data.local.dao.MascotaDao
import com.example.adoptaya.data.local.dao.NotificacionDao
import com.example.adoptaya.data.local.dao.UsuarioDao
import com.example.adoptaya.data.model.Favorito
import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.data.model.Usuario

@Database(
    entities = [Usuario::class, Mascota::class, Notificacion::class, Favorito::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AdoptaYaDatabase : RoomDatabase() {
    abstract fun mascotaDao(): MascotaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun notificacionDao(): NotificacionDao
    abstract fun favoritoDao(): FavoritoDao
}