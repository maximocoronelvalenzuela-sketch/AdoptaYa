package com.example.adoptaya.data.local

import androidx.room.TypeConverter
import com.example.adoptaya.data.model.Enums

class Converters {

    // Para List<String>
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

    // Estado de Mascota
    @TypeConverter
    fun fromEstadoMascota(estado: Enums.EstadoMascota?): String? {
        return estado?.name // Guarda "DISPONIBLE" o "ADOPTADO" como texto
    }

    @TypeConverter
    fun toEstadoMascota(estadoString: String?): Enums.EstadoMascota? {
        if (estadoString == null) return null
        return try {
            Enums.EstadoMascota.valueOf(estadoString)
        } catch (e: Exception) {
            Enums.EstadoMascota.DISPONIBLE // Valor por defecto si hay un error
        }
    }

    // Tipo
    @TypeConverter
    fun fromTipoMascota(tipo: Enums.TipoMascota): String {
        return tipo.name
    }

    @TypeConverter
    fun toTipoMascota(valor: String): Enums.TipoMascota {
        return try {
            Enums.TipoMascota.valueOf(valor)
        } catch (e: Exception) {
            Enums.TipoMascota.PERRO
        }
    }

    // Tiempo
    @TypeConverter
    fun fromTiempo(tiempo: Enums.TiempoEdad): String {
        return tiempo.name
    }

    @TypeConverter
    fun toTiempo(valor: String): Enums.TiempoEdad {
        return try {
            Enums.TiempoEdad.valueOf(valor)
        } catch (e: Exception) {
            Enums.TiempoEdad.AÑOS
        }
    }

    // Sexo
    @TypeConverter
    fun fromSexo(sexo: Enums.SexoMascota): String {
        return sexo.name
    }

    @TypeConverter
    fun toSexo(valor: String): Enums.SexoMascota {
        return try {
            Enums.SexoMascota.valueOf(valor)
        } catch (e: Exception) {
            Enums.SexoMascota.MACHO
        }
    }

    // Tamaño
    @TypeConverter
    fun fromTamaño(tamaño: Enums.TamañoMascota): String {
        return tamaño.name
    }

    @TypeConverter
    fun toTamaño(valor: String): Enums.TamañoMascota {
        return try {
            Enums.TamañoMascota.valueOf(valor)
        } catch (e: Exception) {
            Enums.TamañoMascota.CHICO
        }
    }

    // Para Tipo de Notificacion
    @TypeConverter
    fun fromTipoNotificacion(tipo: Enums.TipoNotificacion?): String? {
        return tipo?.name
    }

    @TypeConverter
    fun toTipoNotificacion(tipoString: String?): Enums.TipoNotificacion? {
        if (tipoString == null) return null
        return try {
            Enums.TipoNotificacion.valueOf(tipoString)
        } catch (e: Exception) {
            Enums.TipoNotificacion.CONTACTO
        }
    }
}