package com.example.adoptaya

import android.app.Application
import androidx.room.Room
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.adoptaya.data.local.AdoptaYaDatabase

class AdoptayaApplication : Application(), ImageLoaderFactory {

    // Unica instancia de la base de datos para toda la app
    val database by lazy {
        Room.databaseBuilder(
            this,
            AdoptaYaDatabase::class.java,
            "adoptaya_database"
        ).fallbackToDestructiveMigration().build()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // Usa hasta el 25% de RAM para fotos instantáneas
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50 * 1024 * 1024) // 50 MB de disco
                    .build()
            }
            .respectCacheHeaders(false) // Fuerza a mantener la foto en caché sin revalidar en la nube
            .build()
    }
}