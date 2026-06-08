package com.example.adoptaya.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "favoritos",
    primaryKeys = ["idUsuario", "idMascota"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE // Si borrás el usuario, se borran sus favoritos
        ),
        ForeignKey(
            entity = Mascota::class,
            parentColumns = ["id"],
            childColumns = ["idMascota"],
            onDelete = ForeignKey.CASCADE // Si borrás la mascota, desaparece de favoritos
        )
    ]
)
data class Favorito (
    val idUsuario: String,
    val idMascota: String
)