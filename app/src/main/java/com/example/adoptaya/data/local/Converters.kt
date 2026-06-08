package com.example.adoptaya.data.local

import androidx.room.TypeConverter
import com.example.adoptaya.data.model.EstadoMascota
import com.example.adoptaya.data.model.TipoNotificacion

class Converters {

    // CONVERTERS PARA LIST<STRING>
    @TypeConverter
    fun fromStringList(lista: List<String>?): String? {
        // Convierte ["perro.jpg", "gato.jpg"] -> "perro.jpg,gato.jpg"
        return lista?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(texto: String?): List<String>? {
        // Convierte "perro.jpg,gato.jpg" -> ["perro.jpg", "gato.jpg"]
        if (texto.isNullOrEmpty()) return emptyList()
        return texto.split(",")
    }

    // CONVERTERS PARA ESTADO MASCOTA
    @TypeConverter
    fun fromEstadoMascota(estado: EstadoMascota?): String? {
        return estado?.name // Guarda "DISPONIBLE" o "ADOPTADO" como texto
    }

    @TypeConverter
    fun toEstadoMascota(estadoString: String?): EstadoMascota? {
        if (estadoString == null) return null
        return try {
            EstadoMascota.valueOf(estadoString)
        } catch (e: Exception) {
            EstadoMascota.DISPONIBLE // Valor por defecto si hay un error
        }
    }

    // CONVERTERS PARA TIPO NOTIFICACION
    @TypeConverter
    fun fromTipoNotificacion(tipo: TipoNotificacion?): String? {
        return tipo?.name
    }

    @TypeConverter
    fun toTipoNotificacion(tipoString: String?): TipoNotificacion? {
        if (tipoString == null) return null
        return try {
            TipoNotificacion.valueOf(tipoString)
        } catch (e: Exception) {
            TipoNotificacion.CONTACTO
        }
    }
}